package com.luis.reflejovision.service.impl;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.MateriaPrimaDAO;
import com.luis.reflejovision.dao.impl.MateriaPrimaDAOImpl;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.MateriaPrimaCriteria;
import com.luis.reflejovision.model.MateriaPrimaDTO;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.service.MateriaPrimaService;

public class MateriaPrimaServiceImpl implements MateriaPrimaService {
	private static Logger logger = LogManager.getLogger(MateriaPrimaServiceImpl.class);
	private MateriaPrimaDAO materiaPrimaDAO = null;
	private Connection con = null;

	public MateriaPrimaServiceImpl() {
		materiaPrimaDAO = new MateriaPrimaDAOImpl();
	}

	@Override
	public MateriaPrimaDTO findbyId(Long id, String locale) throws DataException {
		MateriaPrimaDTO mp = null;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			mp = materiaPrimaDAO.findbyId(con, id, locale);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(con, commit);
		}
		return mp;
	}

	@Override
	public Results<MateriaPrimaDTO> findBy(MateriaPrimaCriteria mp, int pos, int pageSize) throws DataException {
		Results<MateriaPrimaDTO> resultados = null;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			resultados = materiaPrimaDAO.findBy(con, mp, pos, pageSize);
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
	public Long create(MateriaPrimaDTO mp) throws DataException {
		Long id = null;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			id = materiaPrimaDAO.create(con, mp);
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
	public boolean update(MateriaPrimaDTO mp) throws DataException {
		boolean m = false;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			m = materiaPrimaDAO.update(con, mp);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return m;
	}

	@Override
	public boolean delete(Long id) throws DataException {
		boolean mp = false;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			mp = materiaPrimaDAO.delete(con, id);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return mp;
	}
	
}
