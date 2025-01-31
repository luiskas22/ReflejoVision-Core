package com.luis.reflejovision.dao;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.model.UnidadMedida;

public interface UnidadMedidaDAO {
	public List<UnidadMedida> findAll(Connection con) throws DataException;

}