package com.luis.reflejovision.service.impl;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.ProductoDAO;
import com.luis.reflejovision.dao.impl.ProductoDAOImpl;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.model.ProductoCriteria;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.service.ProductoService;
import com.luis.reflejovision.service.StockException;

public class ProductoServiceImpl implements ProductoService {
	private ProductoDAO productoDAO = null;
	private Connection con = null;
	private static Logger logger = LogManager.getLogger(ProductoServiceImpl.class);

	public ProductoServiceImpl() {
		productoDAO = new ProductoDAOImpl();
	}

	@Override
	public Producto findById(Long id) throws DataException {
		Producto p = null;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			p = productoDAO.findById(con, id);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return p;
	}

	@Override
	public Results<Producto> findBy(ProductoCriteria criteria, int pos, int pageSize) throws DataException {
		Connection con = null;
		boolean commit = false;
		
		Results<Producto> resultados = null;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			resultados = productoDAO.findBy(con, criteria, pos, pageSize);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return resultados;
	}

	@Override
	public Long create(Producto p) throws DataException {
		Connection con = null;
		boolean commit = false;
		Long id = null;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			id = productoDAO.create(con, p);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return id;
	}

	@Override
	public boolean update(Producto p) throws DataException {
		boolean pro = false;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			pro = productoDAO.update(con, p);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return pro;
	}

	@Override
	public void updateStock(Long idProducto, Integer unidadesEnStock, Boolean autoActualizacionMateriasPrimas, String locale)
			throws DataException, StockException{
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			productoDAO.updateStock(con, idProducto, unidadesEnStock, autoActualizacionMateriasPrimas, locale);
			commit = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new StockException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
	}

	@Override
	public boolean delete(Long id) throws DataException {
		boolean pro = false;
		Connection con = null;
		boolean commit = false;
		
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			pro = productoDAO.delete(con, id);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return pro;
	}

	

}
