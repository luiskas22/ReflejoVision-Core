package com.luis.reflejovision.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.ConsumoDAO;
import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.MateriaPrimaDAO;
import com.luis.reflejovision.dao.ProductoDAO;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.dao.util.SQLUtils;
import com.luis.reflejovision.model.ConsumoDTO;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.model.ProductoCriteria;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.service.StockException;

public class ProductoDAOImpl implements ProductoDAO {

	private ConsumoDAO consumoDAO = null;
	private MateriaPrimaDAO materiaPrimaDAO = null;
	private static Logger logger = LogManager.getLogger(ProductoDAOImpl.class);

	public ProductoDAOImpl() {
		consumoDAO = new ConsumoDAOImpl();
		materiaPrimaDAO = new MateriaPrimaDAOImpl();
	}

	@Override
	public Producto findById(Connection con, Long id) throws DataException {
		Producto p = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(" SELECT P.ID, P.NOMBRE, P.PRECIO, P.UNIDADES " + " FROM PRODUCTO P "
					+ " WHERE P.ID = " +id);


			rs = pst.executeQuery();

			// Procesa el resultado
			if (rs.next()) {
				p = loadNext(rs, con);
			}

		} catch (SQLException e) {
			logger.error("ID: " + id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return p;
	}

	public Results<Producto> findBy(Connection con, ProductoCriteria criteria, int pos, int pageSize)
			throws DataException {
		Results<Producto> resultados = new Results<>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> condiciones = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder(
					" SELECT P.ID, P.NOMBRE, P.PRECIO, P.UNIDADES " + " FROM PRODUCTO P ");

			// Criterios de búsqueda
			if (criteria.getId() != null) {
				condiciones.add(" P.ID = ? ");
			}
			if (criteria.getNombre() != null) {
				condiciones.add(" P.NOMBRE LIKE ? ");
			}
			if (criteria.getPrecioDesde() != null) {
				condiciones.add(" P.PRECIO >= ? ");
			}
			if (criteria.getPrecioHasta() != null) {
				condiciones.add(" P.PRECIO <= ? ");
			}
			if (criteria.getUnidadesDesde() != null) {
				condiciones.add(" P.UNIDADES >= ? ");
			}
			if (criteria.getUnidadesHasta() != null) {
				condiciones.add(" P.UNIDADES <= ? ");
			}

			if (!condiciones.isEmpty()) {
				query.append(" WHERE ");
				query.append(String.join(" AND ", condiciones));
			}

			query.append(" ORDER BY P.").append(criteria.getOrderBy())
					.append(criteria.getAscDesc() ? " ASC " : " DESC ");

			pst = con.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;
			// Configura parámetros
			if (criteria.getId() != null) {
				pst.setLong(i++, criteria.getId());
			}
			if (criteria.getNombre() != null) {
				pst.setString(i++, SQLUtils.wrapLike(criteria.getNombre()));
			}
			if (criteria.getPrecioDesde() != null) {
				pst.setDouble(i++, criteria.getPrecioDesde());
			}
			if (criteria.getPrecioHasta() != null) {
				pst.setDouble(i++, criteria.getPrecioHasta());
			}
			if (criteria.getUnidadesDesde() != null) {
				pst.setInt(i++, criteria.getUnidadesDesde());
			}
			if (criteria.getUnidadesHasta() != null) {
				pst.setInt(i++, criteria.getUnidadesHasta());
			}

			rs = pst.executeQuery();

			int count = 0;
			// Vamos a la posición inicial de carga
			if ((pos >= 1) && rs.absolute(pos)) {
				// Carga la página de datos
				do {
					resultados.getPage().add(loadNext(rs, con)); // Ajusta `loadNext` si dependía de `locale`
					count++;
				} while (count < pageSize && rs.next());
			}

			// Configura el total de resultados encontrados
			resultados.setTotal(JDBCUtils.getTotalRows(rs));

		} catch (SQLException e) {
			logger.error("Producto criteria: " + criteria, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return resultados;
	}

	@Override
	public Long create(Connection con, Producto p) throws DataException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			pst = con.prepareStatement(" INSERT INTO PRODUCTO(NOMBRE, PRECIO, UNIDADES) " + " VALUES(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			int i = 1;
			pst.setString(i++, p.getNombre());
			pst.setDouble(i++, p.getPrecio());
			pst.setInt(i++, p.getUnidades());

			int insertedRows = pst.executeUpdate();

			if (insertedRows != 1) {
				// throw new ...Exception
			}

			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				Long id = rs.getLong(1);
				p.setId(id);

				consumoDAO.create(con, id, p.getConsumos());

				return id;
			} else {
				// throw new ...Exception
			}

		} catch (SQLException e) {
			logger.error("Producto: " + p, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return null; // por no publicar exception en API

	}

	@Override
	public boolean update(Connection con, Producto p) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(
					" UPDATE PRODUCTO SET " + " NOMBRE= ?, PRECIO= ?, UNIDADES= ? " + "WHERE ID = ? ");

			int i = 1;

			pst.setString(i++, p.getNombre());
			pst.setDouble(i++, p.getPrecio());
			pst.setInt(i++, p.getUnidades());
			pst.setLong(i++, p.getId());

			int updatedRows = pst.executeUpdate();

			if (updatedRows == 0) {
				// No necesariamente es un error, porque pueden
				// haberlo borrado en otro proceso
			} else {
				// Y actualizo sus consumos
				consumoDAO.deleteByProducto(con, p.getId());
				consumoDAO.create(con, p.getId(), p.getConsumos());
				return true;
			}
			// no factible que se actualicen más de una

		} catch (SQLException e) {
			logger.error("Producto: " + p, e);

			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return false;
	}

	@Override
	public boolean delete(Connection con, Long id) throws DataException {
		PreparedStatement pst = null;
		try {
			// Primero suprimo las entidades que cuelgan
			consumoDAO.deleteByProducto(con, id);

			pst = con.prepareStatement(" DELETE FROM PRODUCTO WHERE ID = ?");

			int i = 1;
			pst.setLong(i++, id);

			int deletedRows = pst.executeUpdate();

			if (deletedRows == 0) {
				// No pasa nada realmente, seguramente
				// ha sido ya ha sido borrado en otro proceso
				return false;
			} else {
			}

		} catch (SQLException e) {
			logger.error("ID: " + id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return true;
	}

	protected Producto loadNext(ResultSet rs, Connection con) throws SQLException, DataException {

		int i = 1;

		Producto p = new Producto();

		p.setId(rs.getLong(i++));
		p.setNombre(rs.getString(i++));
		p.setPrecio(rs.getDouble(i++));
		p.setUnidades(rs.getInt(i++));

		List<ConsumoDTO> consumos = consumoDAO.findByProducto(con, p.getId());
		p.setConsumos(consumos);

		return p;
	}

	/** Orientado a inventario */
	public void updateStockInventario(Connection con, Long idProducto, Integer unidadesEnStock) throws DataException {
		PreparedStatement pst = null;
		int i = 1;

		try {
			pst = con.prepareStatement("UPDATE PRODUCTO SET UNIDADES = ? WHERE ID = ?");
			pst.setInt(i++, unidadesEnStock);
			pst.setLong(i++, idProducto);
			int updatedRows = pst.executeUpdate();
		} catch (SQLException e) {
			logger.error("Producto: " + idProducto, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
	}

	@Override
	public void updateStock(Connection con, Long idProducto, Integer variacionStock,
			Boolean actualizacionAutomaticaMateriasPrimas, String locale) throws DataException, StockException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			int i = 1;
			// Actualizar las unidades en stock del producto
			pst = con.prepareStatement("UPDATE PRODUCTO SET UNIDADES = UNIDADES + ? WHERE ID = ?");
			pst.setInt(i++, variacionStock);
			pst.setLong(i++, idProducto);
			int updatedRows = pst.executeUpdate();

			// Reiniciar el índice para la siguiente preparación de declaración
			i = 1;

			// Si se permite la autoactualización de materias primas
			if (actualizacionAutomaticaMateriasPrimas) {
				// Obtener las materias primas consumidas por el producto
				List<ConsumoDTO> consumos = consumoDAO.findByProducto(con, idProducto);

				// Actualizar el stock de las materias primas
				for (ConsumoDTO consumo : consumos) {
					Long idMateriaPrima = consumo.getIdMateriaPrima();
					Double cantidadConsumida = consumo.getUnidades();

					MateriaPrimaDTO mp = materiaPrimaDAO.findbyId(con, idMateriaPrima, locale);

					if (mp.getUnidades() < variacionStock * cantidadConsumida) {
						throw new StockException("Stock insuficiente para la materia prima con ID: " + idMateriaPrima);
					}
					// Reiniciar el índice para la siguiente preparación de declaración
					i = 1;

					// Actualizar las unidades en stock de la materia prima
					pst = con.prepareStatement("UPDATE MATERIAPRIMA SET UNIDADES = UNIDADES - ? " + " WHERE ID = ?");
					pst.setDouble(i++, variacionStock * cantidadConsumida);
					pst.setLong(i++, idMateriaPrima);
					pst.executeUpdate();
				}
			}

		} catch (SQLException e) {
			logger.error("Producto: " + idProducto, e);
			throw new StockException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
	}

}