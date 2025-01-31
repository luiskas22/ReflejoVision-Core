package com.luis.reflejovision.dao;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.model.Rol;

public interface RolDAO {
	public List<Rol> findAll(Connection con) throws DataException;

}
