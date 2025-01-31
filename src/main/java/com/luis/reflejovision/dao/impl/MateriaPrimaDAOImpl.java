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

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.MateriaPrimaDAO;
import com.luis.reflejovision.dao.UnidadMedidaDAO;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.dao.util.SQLUtils;
import com.luis.reflejovision.model.MateriaPrimaCriteria;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.model.MateriaPrimaIdioma;
import com.luis.reflejovision.model.Results;

public class MateriaPrimaDAOImpl implements MateriaPrimaDAO {
	private UnidadMedidaDAO unidadMedidaDAO = null;
	private static Logger logger = LogManager.getLogger(MateriaPrimaDAOImpl.class);

	public MateriaPrimaDAOImpl() {
		unidadMedidaDAO = new UnidadMedidaDAOImpl();
	}

	@Override
	public MateriaPrimaDTO findbyId(Connection con, Long id, String locale) throws DataException {
		MateriaPrimaDTO mp = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// Consulta modificada para obtener todas las traducciones
			pst = con.prepareStatement(
					"SELECT MP.ID, MPI.NOMBRE, MP.PRECIO, MP.UNIDADES, MP.UNIDADMEDIDA_ID, I.nombre AS idioma "
							+ " FROM MATERIAPRIMA MP"
							+ " INNER JOIN MATERIAPRIMA_IDIOMA MPI ON MP.ID = MPI.ID_MATERIAPRIMA"
							+ " INNER JOIN IDIOMA I ON MPI.ID_IDIOMA = I.ID " + " WHERE MP.ID = ?");

			pst.setLong(1, id);

			rs = pst.executeQuery();

			List<MateriaPrimaIdioma> traducciones = new ArrayList<>();

			while (rs.next()) {
				if (mp == null) {
					mp = new MateriaPrimaDTO();
					mp.setId(rs.getLong("ID"));
					mp.setPrecio(rs.getDouble("PRECIO"));
					mp.setUnidades(rs.getInt("UNIDADES"));
					mp.setIdUnidadMedida(rs.getLong("UNIDADMEDIDA_ID"));
				}

				MateriaPrimaIdioma traduccion = new MateriaPrimaIdioma();
				traduccion.setIdMateriaPrima(rs.getLong("ID"));
				traduccion.setLocale(rs.getString("IDIOMA"));
				traduccion.setNombre(rs.getString("NOMBRE"));
				traducciones.add(traduccion);

				// Establecer el nombre solo si coincide con el locale
				if (locale.equalsIgnoreCase(rs.getString("IDIOMA"))) {
					mp.setNombre(rs.getString("NOMBRE"));
				}
			}

			if (mp != null) {
				mp.setTraducciones(traducciones);
			}

		} catch (SQLException e) {
			logger.error("Error al buscar MateriaPrima por ID: " + id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}

		return mp;
	}

	public Results<MateriaPrimaDTO> findBy(Connection con, MateriaPrimaCriteria criteria, int pos, int pageSize)
			throws DataException {
		Results<MateriaPrimaDTO> resultados = new Results<MateriaPrimaDTO>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> condiciones = new ArrayList<String>();

		try {
			StringBuilder query = new StringBuilder(
					"SELECT MP.ID, MPI.NOMBRE, MP.PRECIO, MP.UNIDADES, MP.UNIDADMEDIDA_ID " + " FROM MATERIAPRIMA MP"
							+ " INNER JOIN MATERIAPRIMA_IDIOMA MPI ON MP.ID = MPI.ID_MATERIAPRIMA "
							+ " INNER JOIN IDIOMA I ON MPI.ID_IDIOMA = I.ID WHERE I.ID = ? ");

			if (criteria.getId() != null) {
				condiciones.add("MP.ID = ?");
			}
			if (criteria.getNombre() != null) {
				condiciones.add("MPI.NOMBRE LIKE ?");
			}
			if (criteria.getPrecioDesde() != null) {
				condiciones.add("MP.PRECIO >= ?");
			}
			if (criteria.getPrecioHasta() != null) {
				condiciones.add("MP.PRECIO <= ?");
			}
			if (criteria.getUnidadesDesde() != null) {
				condiciones.add("MP.UNIDADES >= ?");
			}
			if (criteria.getUnidadesHasta() != null) {
				condiciones.add("MP.UNIDADES <= ?");
			}

			if (!condiciones.isEmpty()) {
				query.append(condiciones.isEmpty() ? " WHERE " : " AND ");
				query.append(String.join(" AND ", condiciones));
			}

			query.append(" ORDER BY MP.").append(criteria.getOrderBy())
					.append(criteria.getAscDesc() ? " ASC " : " DESC ");

			String sql = query.toString();

			pst = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i = 1;

			pst.setString(i++, criteria.getLocale()); // Establecer locale

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
			// Vamos a la posicion inicial de carga
			if ((pos >= 1) && rs.absolute(pos)) {
				// Cargo la página de datos
				do {
					resultados.getPage().add(loadNext(rs));
					count++;
				} while (count < pageSize && rs.next());
			}

			// Seteo el total de resultados encontrados
			resultados.setTotal(JDBCUtils.getTotalRows(rs));

		} catch (SQLException e) {
			logger.error("Materia prima criteria: " + criteria, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return resultados;
	}

	@Override
	public Long create(Connection con, MateriaPrimaDTO mp) throws DataException {
		PreparedStatement pstMain = null;
		PreparedStatement pstLocale = null;
		ResultSet rs = null;

		try {
			// 1. Inserción en la tabla principal MATERIAPRIMA
			pstMain = con.prepareStatement(
					"INSERT INTO MATERIAPRIMA(NOMBRE, PRECIO, UNIDADES, UNIDADMEDIDA_ID) " + "VALUES(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			int i = 1;
			pstMain.setString(i++, mp.getNombre()); // NOMBRE principal (en idioma por defecto)
			pstMain.setDouble(i++, mp.getPrecio());
			pstMain.setInt(i++, mp.getUnidades());
			pstMain.setLong(i++, mp.getIdUnidadMedida());

			int insertedRows = pstMain.executeUpdate();
			if (insertedRows != 1) {
				throw new DataException("Failed to insert MateriaPrima");
			}

			// Obtener el ID generado para la nueva MateriaPrima
			rs = pstMain.getGeneratedKeys();
			if (rs.next()) {
				Long id = rs.getLong(1);
				mp.setId(id); // Establecer el ID generado en el objeto MateriaPrimaDTO

				// 2. Insertar las traducciones en la tabla MATERIAPRIMA_IDIOMA
				try {
					pstLocale = con.prepareStatement(
							"INSERT INTO MATERIAPRIMA_IDIOMA(NOMBRE, ID_MATERIAPRIMA, ID_IDIOMA) " + "VALUES(?, ?, ?)");

					// Insertar cada traducción individualmente
					for (MateriaPrimaIdioma traduccion : mp.getTraducciones()) {
						int j = 1;
						pstLocale.setString(j++, traduccion.getNombre());
						pstLocale.setLong(j++, mp.getId()); // Usar el ID generado de MateriaPrima
						pstLocale.setString(j++, traduccion.getLocale()); // "es", "en", "gl", etc.
						pstLocale.executeUpdate(); // Ejecutar la inserción para cada traducción
					}

				} catch (SQLException e) {
					throw new DataException("Error inserting translations into MATERIAPRIMA_IDIOMA", e);
				} finally {
					JDBCUtils.close(pstLocale); // Cerrar PreparedStatement de traducciones
				}

				return id; // Devolver el ID generado para MateriaPrima
			} else {
				throw new DataException("Failed to retrieve generated key for MateriaPrima");
			}

		} catch (SQLException e) {
			logger.error("Error creating Materia Prima: " + mp, e);
			throw new DataException(e);
		} finally {
			// Cerrar recursos de la tabla principal
			JDBCUtils.close(pstMain, rs);
		}
	}

	@Override
	public boolean update(Connection con, MateriaPrimaDTO mp) throws DataException {
		PreparedStatement pstMain = null;
		PreparedStatement pstLocale = null;

		try {
			// Actualizar la tabla principal MATERIAPRIMA
			pstMain = con.prepareStatement(
					"UPDATE MATERIAPRIMA SET NOMBRE=?, PRECIO = ?, UNIDADES = ?, UNIDADMEDIDA_ID = ? WHERE ID = ?");
			int i = 1;
			pstMain.setString(i++, mp.getNombre());
			pstMain.setDouble(i++, mp.getPrecio());
			pstMain.setInt(i++, mp.getUnidades());
			pstMain.setLong(i++, mp.getIdUnidadMedida());
			pstMain.setLong(i++, mp.getId());

			int updatedRowsMain = pstMain.executeUpdate();
			if (updatedRowsMain == 0)
				return false; // Si no se actualizó nada en la tabla principal

		} catch (SQLException e) {
			throw new DataException("Error updating MATERIAPRIMA", e);
		} finally {
			JDBCUtils.close(pstMain);
		}

		try {
			// Actualizar la tabla de idiomas MATERIAPRIMA_IDIOMA
			pstLocale = con.prepareStatement(
					"UPDATE MATERIAPRIMA_IDIOMA SET NOMBRE = ? WHERE ID_MATERIAPRIMA = ? AND ID_IDIOMA = ?");

			for (MateriaPrimaIdioma traduccion : mp.getTraducciones()) {
				int j = 1; // Reiniciar índice para cada traducción
				pstLocale.setString(j++, traduccion.getNombre());
				pstLocale.setLong(j++, mp.getId());
				pstLocale.setString(j++, traduccion.getLocale());
				pstLocale.executeUpdate();
			}

			return true; // Si todas las actualizaciones fueron exitosas
		} catch (SQLException e) {
			throw new DataException("Error updating MATERIAPRIMA_IDIOMA", e);
		} finally {
			JDBCUtils.close(pstLocale);
		}
	}

	@Override
	public boolean delete(Connection con, Long id) throws DataException {
	    PreparedStatement pstDeleteLocale = null;
	    PreparedStatement pstDeleteMain = null;

	    try {
	        // Eliminar las filas dependientes de 'materiaprima_idioma'
	        pstDeleteLocale = con.prepareStatement("DELETE FROM materiaprima_idioma WHERE id_materiaprima = ?");
	        pstDeleteLocale.setLong(1, id);
	        pstDeleteLocale.executeUpdate();

	        // Ahora eliminar el registro principal de 'materiaprima'
	        pstDeleteMain = con.prepareStatement("DELETE FROM MATERIAPRIMA WHERE ID = ?");
	        pstDeleteMain.setLong(1, id);

	        int deletedRows = pstDeleteMain.executeUpdate();

	        if (deletedRows == 0) {
	            // Si no se eliminó ninguna fila, el registro ya fue eliminado
	            return false;
	        }

	    } catch (SQLException e) {
	        logger.error("ID: " + id, e);
	        throw new DataException(e);
	    } finally {
	        // Cerrar los PreparedStatements
	        JDBCUtils.close(pstDeleteLocale);
	        JDBCUtils.close(pstDeleteMain);
	    }

	    return true;
	}


	protected MateriaPrimaDTO loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		MateriaPrimaDTO mp = new MateriaPrimaDTO();

		mp.setId(rs.getLong(i++));
		mp.setNombre(rs.getString(i++));
		mp.setPrecio(rs.getDouble(i++));
		mp.setUnidades(rs.getInt(i++));
		mp.setIdUnidadMedida(rs.getLong(i++));
		return mp;
	}

}
