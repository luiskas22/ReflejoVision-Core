package com.luis.reflejovision.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.ConsumoDAO;
import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.ConsumoDTO;

public class ConsumoDAOImpl implements ConsumoDAO {
	private static Logger logger = LogManager.getLogger(ConsumoDAOImpl.class);

	@Override
	public List<ConsumoDTO> findByProducto(Connection con, Long idProducto) throws DataException {

		List<ConsumoDTO> consumos = new ArrayList<ConsumoDTO>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT C.ID_PRODUCTO, C.ID_MATERIAPRIMA, MP.NOMBRE, MP.UNIDADMEDIDA_ID, MP.PRECIO, C.UNIDADES_CONSUMO " +
		             "FROM CONSUMO C " +
		             "INNER JOIN MATERIAPRIMA MP ON MP.ID = C.ID_MATERIAPRIMA " +
		             "WHERE C.ID_PRODUCTO = ?";

			pst = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;

			pst.setLong(i++, idProducto);
			rs = pst.executeQuery();

			while (rs.next()) {
				consumos.add(loadNext(rs, con));
			}

		} catch (SQLException e) {
			logger.error("ID: " + idProducto, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return consumos;
	}


	@Override
	public void create(Connection con, ConsumoDTO c) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(
					"INSERT INTO CONSUMO(ID_PRODUCTO, ID_MATERIAPRIMA, UNIDADES_CONSUMO) VALUES(?,?,?)");

			int i = 1;
			pst.setLong(i++, c.getIdProducto());
			pst.setLong(i++, c.getIdMateriaPrima());
			pst.setDouble(i++, c.getUnidades());

			int insertedRows = pst.executeUpdate();

			if (insertedRows != 1) {
				// throw new ...Exception
			}

		} catch (SQLException e) {
			logger.error("Consumo: " + c, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
	}

	@Override
	public void create(Connection con, Long idProducto, List<ConsumoDTO> consumos) throws DataException {
		PreparedStatement pst = null;
		if (consumos.size() == 0)
			return;

		try {

			StringBuilder query = new StringBuilder(
					" INSERT INTO CONSUMO(ID_PRODUCTO, ID_MATERIAPRIMA, UNIDADES_CONSUMO) " + " VALUES ");

			query = JDBCUtils.appendMultipleInsertParameters(query, "(?,?,?)", consumos.size());

			pst = con.prepareStatement(query.toString());

			int i = 1;
			for (ConsumoDTO consumo : consumos) {
				consumo.setIdProducto(idProducto);
				pst.setLong(i++, consumo.getIdProducto());
				pst.setLong(i++, consumo.getIdMateriaPrima());
				pst.setDouble(i++, consumo.getUnidades());
			}
			;

			int insertedRows = pst.executeUpdate();

			if (insertedRows != consumos.size()) {
				// throw new Excepction
			}

		} catch (SQLException e) {
			logger.error("Consumo " + idProducto, consumos, e);

			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}

	}

	@Override
	public boolean update(Connection con, ConsumoDTO consumo) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(" UPDATE CONSUMO " + " SET UNIDADES_CONSUMO = ? " + " WHERE ID_PRODUCTO = ? "
					+ " AND ID_MATERIAPRIMA = ? ");
			int i = 1;

			pst.setDouble(i++, consumo.getUnidades());
			pst.setLong(i++, consumo.getIdProducto());
			pst.setLong(i++, consumo.getIdMateriaPrima());

			int updatedRows = pst.executeUpdate();

			if (updatedRows != 1) {
				// Normalmente será porque lo ha borrado
				// otro proceso
				return false;
			}

		} catch (SQLException e) {
			logger.error("Consumo: " + consumo, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return true;
	}

	@Override
	public boolean delete(Connection con, Long idProducto, Long idMateriaPrima) throws DataException {
		PreparedStatement pst = null;

		try {

			pst = con.prepareStatement(
					" DELETE FROM CONSUMO " + " WHERE ID_PRODUCTO = ? " + " AND ID_MATERIAPRIMA = ? ");
			int i = 1;

			pst.setLong(i++, idProducto);
			pst.setLong(i++, idMateriaPrima);

			int updatedRows = pst.executeUpdate();

			if (updatedRows != 1) {
				// Normalmente será porque lo ha borrado
				// otro proceso
				return false;
			}

		} catch (SQLException e) {
			logger.error("ID: " + idProducto, idMateriaPrima, e);

			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return true;
	}

	@Override
	public boolean deleteByProducto(Connection con, Long idProducto) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(" DELETE FROM CONSUMO " + " WHERE ID_PRODUCTO = ? ");

			int i = 1;

			pst.setLong(i++, idProducto);

			int updatedRows = pst.executeUpdate();

			if (updatedRows < 1) {
				// Normalmente será porque lo ha borrado
				// otro proceso
				return false;
			}

		} catch (SQLException e) {
			logger.error("ID: " + idProducto, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return true;
	}

	protected ConsumoDTO loadNext(ResultSet rs, Connection con) throws SQLException {

		int i = 1;

		ConsumoDTO c = new ConsumoDTO();

		c.setIdProducto(rs.getLong(i++));
		c.setIdMateriaPrima(rs.getLong(i++));
		c.setNombreMateriaPrima(rs.getString(i++));
		c.setIdUnidadMedidaMp(rs.getInt(i++));
		c.setPrecioMateriaPrima(rs.getDouble(i++));
		c.setUnidades(rs.getDouble(i++));
		return c;
	}


	
}
