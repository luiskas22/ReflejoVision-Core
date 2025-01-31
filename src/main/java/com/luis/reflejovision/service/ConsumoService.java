package com.luis.reflejovision.service;

import java.util.List;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.model.ConsumoDTO;

public interface ConsumoService {
	public List<ConsumoDTO> findByProducto(Long idProducto) throws DataException;

	public void create(ConsumoDTO consumo) throws DataException;

	public void create(Long idProducto, List<ConsumoDTO> consumos) throws DataException;

	public boolean update(ConsumoDTO consumo) throws DataException;

	public boolean delete(Long idProducto, Long idMateriaPrima) throws DataException;

	public boolean deleteByProducto(Long idProducto) throws DataException;

}
