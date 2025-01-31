package com.luis.reflejovision.dao;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.model.ConsumoDTO;

public interface ConsumoDAO {

	public List<ConsumoDTO> findByProducto(Connection con, Long idProducto) throws DataException;

	public void create(Connection con, ConsumoDTO consumo) throws DataException;

	public void create(Connection con, Long idProducto, List<ConsumoDTO> consumos) throws DataException;

	public boolean update(Connection con, ConsumoDTO consumo) throws DataException;

	public boolean delete(Connection con, Long idProducto, Long idMateriaPrima) throws DataException;
 
	public boolean deleteByProducto(Connection con, Long idProducto) throws DataException;

}
