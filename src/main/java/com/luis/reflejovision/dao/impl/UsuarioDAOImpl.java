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
import com.luis.reflejovision.dao.RolDAO;
import com.luis.reflejovision.dao.UsuarioDAO;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.dao.util.SQLUtils;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.model.Usuario;
import com.luis.reflejovision.model.UsuarioCriteria;

public class UsuarioDAOImpl implements UsuarioDAO {
	private RolDAO rolDAO = null;
	private static Logger logger = LogManager.getLogger(UsuarioDAOImpl.class);

	public UsuarioDAOImpl() {
		rolDAO = new RolDAOImpl();
	}

	@Override
	public Usuario findbyId(Connection con, Long id) throws DataException {
		Usuario u = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			stmt = con.createStatement();

			rs = stmt.executeQuery(" SELECT ID, USERNAME, NOMBRE, ID_ROL , CORREO " + " FROM USUARIO "
					+ " WHERE ID = " + id);

			if (rs.next()) {
				u = loadNext(rs);
			}

		} catch (SQLException e) {
			logger.error("ID: " + id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(stmt, rs);
		}
		return u;
	}

	public Results<Usuario> findBy(Connection con, UsuarioCriteria criteria, int pos, int pageSize)
			throws DataException {
		Results<Usuario> resultados = new Results<Usuario>();
		List<String> condiciones = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			StringBuilder query = new StringBuilder(
					"SELECT ID, USERNAME, NOMBRE, ID_ROL, CORREO  FROM USUARIO");

			if (criteria.getId() != null) {
				condiciones.add("ID = ?");
			}
			if (criteria.getUsername() != null) {
				condiciones.add("USERNAME LIKE ?");
			}
			if (criteria.getNombre() != null) {
				condiciones.add("NOMBRE LIKE ?");
			}
			if (criteria.getRol() != null) {
				condiciones.add("ID_ROL = ?");
			}

			if (criteria.getCorreo() != null) {
				condiciones.add("CORREO LIKE ?");
			}

			if (!condiciones.isEmpty()) {
				query.append(" WHERE ");
				query.append(String.join(" AND ", condiciones));
			}

			String sql = query.toString();
			pst = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i = 1;

			if (criteria.getId() != null) {
				pst.setLong(i++, criteria.getId());
			}
			if (criteria.getUsername() != null) {
				pst.setString(i++, SQLUtils.wrapLike(criteria.getUsername()));
			}

			if (criteria.getNombre() != null) {
				pst.setString(i++, SQLUtils.wrapLike(criteria.getNombre()));
			}
			if (criteria.getRol() != null) {
				pst.setLong(i++, criteria.getRol());
			}
			if (criteria.getCorreo() != null) {
				pst.setString(i++, SQLUtils.wrapLike(criteria.getCorreo()));
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
			logger.error("Usuario criteria: " + criteria, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return resultados;
	}

	@Override
	public Long create(Connection con, Usuario u) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(
					" INSERT INTO USUARIO(CONTRASENA, USERNAME, NOMBRE, ID_ROL, CORREO) " + " VALUES(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			int i = 1;
			pst.setString(i++, u.getContrasena());
			pst.setString(i++, u.getUsername());
			pst.setString(i++, u.getNombre());
			pst.setLong(i++, u.getRol());
			pst.setString(i++, u.getCorreo());

			int insertedRows = pst.executeUpdate();

			if (insertedRows != 1) {
				// throw new ...Exception
			} else {

				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
					u.setId(id);
					rolDAO.findAll(con);
					return id;
				} else {
					// throw new ...Exception
				}
			}
		} catch (SQLException e) {
			logger.error("Usuario: " + u, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return null; // por no publicar exception en API
	}

	@Override
	public boolean update(Connection con, Usuario u) throws DataException {
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement(" UPDATE USUARIO SET " + " CONTRASENA= ?, USERNAME = ?, NOMBRE= ?,"
					+ " ID_ROL= ?, CORREO= ? " + "WHERE ID = ? ");

			int i = 1;
			pst.setString(i++, u.getContrasena());
			pst.setString(i++, u.getUsername());
			pst.setString(i++, u.getNombre());
			pst.setLong(i++, u.getRol());
			pst.setString(i++, u.getCorreo());
			pst.setLong(i++, u.getId());

			int updatedRows = pst.executeUpdate();

			if (updatedRows == 0) {
				// No necesariamente es un error, porque pueden
				// haberlo borrado en otro proceso
			} else {
				return true;
			}
			// no factible que se actualicen más de una

		} catch (SQLException e) {
			logger.error("Usuario: " + u, e);
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

			pst = con.prepareStatement(" DELETE FROM USUARIO WHERE ID = ?");

			int i = 1;
			pst.setLong(i++, id);

			int deletedRows = pst.executeUpdate();

			if (deletedRows == 0) {
				// No pasa nada realmente, seguramente
				// ha sido ya ha sido borrado en otro proceso
				return false;
			}

		} catch (SQLException e) {
			logger.error("ID: " + id, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst);
		}
		return true;
	}

	protected Usuario loadNext(ResultSet rs) throws SQLException {

		int i = 1;

		Usuario u = new Usuario();

		u.setId(rs.getLong(i++));
		u.setUsername(rs.getString(i++));
		u.setNombre(rs.getString(i++));
		u.setRol(rs.getLong(i++));
		u.setCorreo(rs.getString(i++));

		return u;
	}

	@Override
	public Usuario findByNick(Connection con, String nick) throws DataException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Usuario u = new Usuario();
		try {

			StringBuilder query = new StringBuilder(" SELECT ID, USERNAME, NOMBRE, ID_ROL, CORREO ")
					.append(" FROM USUARIO ").append(" WHERE USERNAME = ? ");

			pst = con.prepareStatement(query.toString(), ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);

			int i = 1;
			pst.setString(i++, nick);

			rs = pst.executeQuery();

			if (rs.next()) {
				u = loadNext(rs);
			}

		} catch (SQLException e) {
			logger.error("Nickname: " + nick, e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(pst, rs);
		}
		return u;
	}
}
