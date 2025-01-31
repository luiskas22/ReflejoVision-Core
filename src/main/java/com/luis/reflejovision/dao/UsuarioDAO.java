package com.luis.reflejovision.dao;

import java.sql.Connection;
import java.util.List;

import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.model.Usuario;
import com.luis.reflejovision.model.UsuarioCriteria;


public interface UsuarioDAO {
	public Usuario findbyId(Connection con, Long id) throws DataException;

	public Results<Usuario> findBy(Connection con, UsuarioCriteria criteria, int pos, int pageSize) throws DataException;

	public Usuario findByNick(Connection con, String nick) throws DataException;

	public Long create(Connection con, Usuario u) throws DataException;

	public boolean update(Connection con, Usuario u) throws DataException;

	public boolean delete(Connection con, Long id) throws DataException;
}
