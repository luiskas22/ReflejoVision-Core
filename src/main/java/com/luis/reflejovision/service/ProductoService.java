package com.luis.reflejovision.service;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.model.ProductoCriteria;
import com.luis.reflejovision.model.Results;

public interface ProductoService {

	public Producto findById(Long id) throws DataException;

	public Results<Producto> findBy(ProductoCriteria criteria, int pos, int pageSize) throws DataException;

	public Long create(Producto p) throws DataException;

	public boolean update(Producto p) throws DataException;

	/**
	 * Actualiza las unidades en stock del producto, y, de acuerdo a los datos de
	 * consumos,también el stock de las materias primas.
	 * 
	 * @param idProducto
	 * @param unidadesEnStock
	 * @param autoActualizacionMateriasPrimas Permite decidir si el stock de
	 *                                        materias primas se actualiza (o no)
	 *                                        automaticamente en funcion de los
	 *                                        consumos de este producto y los
	 *                                        consumos.
	 * @throws StockException 
	 */
	public void updateStock(Long idProducto, Integer unidadesEnStock, Boolean autoActualizacionMateriasPrimas, String locale)
			throws DataException, StockException;

	public boolean delete(Long id) throws DataException;

}
