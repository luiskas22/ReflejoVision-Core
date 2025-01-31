package com.luis.reflejovision.service;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.model.Results;
import com.luis.reflejovision.model.Usuario;
import com.luis.reflejovision.model.UsuarioCriteria;

public interface UsuarioService {
	public Results<Usuario> findBy(UsuarioCriteria criteria, int pos, int pageSize) throws DataException;

	public Usuario findById(Long id) throws DataException;

	public Usuario autenticar(String userName, String password) throws DataException;

	public Long registrar(Usuario u) throws DataException, ServiceException;

	public boolean update(Usuario u) throws DataException, ServiceException;

	public boolean delete(Long id) throws DataException, ServiceException;
}
