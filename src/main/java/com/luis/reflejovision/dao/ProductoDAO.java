package com.luis.reflejovision.dao;

import java.sql.Connection;

import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.model.ProductoCriteria;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.service.StockException;

public interface ProductoDAO {

	public Producto findById(Connection con, Long id) throws DataException;

	public Results<Producto> findBy(Connection con, ProductoCriteria criteria, int pos, int pageSize)
			throws DataException;

	public Long create(Connection con, Producto p) throws DataException;

	/*
	 * Actualiza las unidades en stock del producto, y, de acuerdo a los datos de
	 * consumos,también el stock de las materias primas.
	 * 
	 * @param idProducto
	 * 
	 * @param unidadesEnStock
	 * 
	 * @param autoActualizacionMateriasPrimas Permite decidir si el stock de
	 * materias primas se actualiza (o no) automaticamente en funcion de los
	 * consumos de este producto y los consumos.
	 */
	public void updateStock(Connection con, Long idProducto, Integer diferencial,
			Boolean autoActualizacionMateriasPrimas, String locale) throws DataException, StockException;

	public void updateStockInventario(Connection con, Long idProducto, Integer unidadesEnStock) throws DataException;

	public boolean update(Connection con, Producto p) throws DataException;

	public boolean delete(Connection con, Long id) throws DataException;

}
