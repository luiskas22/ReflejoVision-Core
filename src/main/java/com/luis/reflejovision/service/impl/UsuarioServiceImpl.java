package com.luis.reflejovision.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.dao.UsuarioDAO;
import com.luis.reflejovision.dao.impl.UsuarioDAOImpl;
import com.luis.reflejovision.dao.util.JDBCUtils;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.model.Usuario;
import com.luis.reflejovision.model.UsuarioCriteria;
import com.luis.reflejovision.service.MailService;
import com.luis.reflejovision.service.ServiceException;
import com.luis.reflejovision.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService {
	/**
	 * Es un objeto stateless, no tiene sentido instanciarlo multiples veces. (NOTA:
	 * Para segundo curso de DAW: Será un atributo, no una constante, para que
	 * podamos inyectarlo, y si queremos, cambiar el Encryptor).
	 */
	public static final StrongPasswordEncryptor PASSWORD_ENCRYPTOR = new StrongPasswordEncryptor();
	private UsuarioDAO usuarioDAO = null;
	private Connection con = null;
	private static Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);
	private MailService mailService = null;

	public UsuarioServiceImpl() {
		usuarioDAO = new UsuarioDAOImpl();
		mailService = new MailServiceImpl();
	}

	@Override
	public Results<Usuario> findBy(UsuarioCriteria criteria, int pos, int pageSize) throws DataException {
		Results<Usuario> resultados = null;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			resultados = usuarioDAO.findBy(con, criteria, pos, pageSize);
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
	public Usuario autenticar(String userName, String password) throws DataException {

		Usuario usuario = null;
//		UsuarioCriteria porUserName = new UsuarioCriteria();
//		porUserName.setUsername(userName);

		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			usuario = usuarioDAO.findByNick(con, userName);
			if (usuario == null) {
				return null;
			}

			if (!PASSWORD_ENCRYPTOR.checkPassword(password, usuario.getContrasena())) {
				usuario = null;
			}

			commit = true;

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.close(con, commit);
		}
		return usuario;
	}

	@Override
	public Long registrar(Usuario u) throws DataException, ServiceException {

		// Encriptamos la password del usuario para guardarla en BD.
		// (Puede definirse otro atributo diferente en Cliente para la encriptada).
		u.setContrasena(PASSWORD_ENCRYPTOR.encryptPassword(u.getContrasena()));

		Long id = null;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			id = usuarioDAO.create(con, u);
//			mailService.enviar(u.getCorreo(), "Bienvenido a nuestro equipo de trabajo",
//					"Buenas, soy Luis López creador de reflejovision " + " te doy la bienvenida a nuestra corporación."
//							+ "Este mail se envia debido a que te he registrado en mi base de datos de mi aplicacion =)");
			commit = true;

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return id;
	}

	@Override
	public boolean update(Usuario u) throws DataException, ServiceException {
		boolean user = false;
		Connection con = null;
		boolean commit = false;
		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			user = usuarioDAO.update(con, u);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return user;
	}

	@Override
	public boolean delete(Long id) throws DataException, ServiceException {
		boolean user = false;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			user = usuarioDAO.delete(con, id);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return user;
	}

	@Override
	public Usuario findById(Long id) throws DataException {
		Usuario u = null;
		Connection con = null;
		boolean commit = false;

		try {
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			u = usuarioDAO.findbyId(con, id);
			commit = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataException(e);

		} finally {
			JDBCUtils.close(con, commit);
		}
		return u;
	}

}
