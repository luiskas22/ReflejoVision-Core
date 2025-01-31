package com.luis.reflejovision.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.RolDAO;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.Rol;
import com.luis.reflejovision.model.Rol;

public class RolDAOImpl implements RolDAO{

	public List<Rol> findAll(Connection con) throws DataException{
		List<Rol> roles = new ArrayList<Rol>();
		Rol rol = null;
		ResultSet rs = null;
		PreparedStatement pst = null;

		try {

			StringBuilder query = new StringBuilder(" SELECT ID, NOMBRE").append(" FROM ROL ");

			pst = con.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;

			rs = pst.executeQuery();

			while (rs.next()) {
				roles.add(loadNext(con, rs));

			}

		} catch (SQLException e) {
			throw new DataException(e);

		} finally {
			JDBCUtils.close(pst, rs);
		}

		return roles;

	}

	
	protected Rol loadNext(Connection con, ResultSet rs) throws SQLException {

		int i = 1;
		Rol rol = new Rol();

		rol.setId(rs.getLong(i++));
		rol.setNombre(rs.getString(i++));

		return rol;
	}

}

