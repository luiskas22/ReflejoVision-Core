package com.luis.reflejovision.service;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.model.MateriaPrimaCriteria;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.model.Results;

public interface MateriaPrimaService {
	public MateriaPrimaDTO findbyId(Long id, String locale) throws DataException;

	public Results<MateriaPrimaDTO> findBy(MateriaPrimaCriteria mp, int pos, int pageSize) throws DataException;

	public Long create(MateriaPrimaDTO mp) throws DataException;

	public boolean update(MateriaPrimaDTO mp) throws DataException;

	public boolean delete(Long id) throws DataException;
}
