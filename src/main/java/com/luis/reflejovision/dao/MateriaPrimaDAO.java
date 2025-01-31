package com.luis.reflejovision.dao;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.model.MateriaPrimaCriteria;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.model.Results;

public interface MateriaPrimaDAO {

	public MateriaPrimaDTO findbyId(Connection con, Long id, String locale) throws DataException;

	public Results<MateriaPrimaDTO> findBy(Connection con, MateriaPrimaCriteria mp, int pos, int pageSize)
			throws DataException;

	public Long create(Connection con, MateriaPrimaDTO materiaPrima) throws DataException;

	public boolean update(Connection con, MateriaPrimaDTO materiaPrima) throws DataException;

	public boolean delete(Connection con, Long id) throws DataException;

}
