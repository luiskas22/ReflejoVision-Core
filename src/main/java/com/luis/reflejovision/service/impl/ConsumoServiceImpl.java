package com.luis.reflejovision.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.ConsumoDAO;
import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.impl.ConsumoDAOImpl;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.ConsumoDTO;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.service.ConsumoService;

public class ConsumoServiceImpl implements ConsumoService {
	private ConsumoDAO consumoDAO = null;
	private static Logger logger = LogManager.getLogger(ConsumoServiceImpl.class);

	private Connection con = null;

	public ConsumoServiceImpl() {
		consumoDAO = new ConsumoDAOImpl();
	}

	@Override
	public List<ConsumoDTO> findByProducto(Long idProducto) throws DataException {
		List<ConsumoDTO> consumos = new ArrayList<ConsumoDTO>();
		MateriaPrimaDTO mp = null;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			consumos = consumoDAO.findByProducto(con, idProducto);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return consumos;
	}

	@Override
	public void create(ConsumoDTO consumo) throws DataException {
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);

			consumoDAO.create(con, consumo);

			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
	}

	@Override
	public void create(Long idProducto, List<ConsumoDTO> consumos) throws DataException {
		Connection con = null;
		boolean commit = false;
		Long id = null;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			consumoDAO.create(con, idProducto, consumos);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(con, commit);
		}
	}

	@Override
	public boolean update(ConsumoDTO consumo) throws DataException {
		Connection con = null;
		boolean commit = false;
		boolean c = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			c = consumoDAO.update(con, consumo);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(con, commit);
		}
		return c;
	}

	@Override
	public boolean delete(Long idProducto, Long idMateriaPrima) throws DataException {
		Connection con = null;
		boolean commit = false;
		boolean c = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			c = consumoDAO.delete(con, idProducto, idMateriaPrima);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return c;
	}

	@Override
	public boolean deleteByProducto(Long idProducto) throws DataException {
		Connection con = null;
		boolean commit = false;
		
		boolean c = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			c = consumoDAO.deleteByProducto(con, idProducto);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return c;
	}

}
