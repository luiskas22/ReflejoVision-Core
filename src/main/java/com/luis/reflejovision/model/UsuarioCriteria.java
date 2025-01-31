package com.luis.reflejovision.model;

public class UsuarioCriteria {
	private Long id = null;

	private Long id_rol = null;

	private String nombre = null;

	private String correo = null;

	private String username = null;

	public UsuarioCriteria() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRol() {
		return id_rol;
	}

	public void setRol(Long rol) {
		this.id_rol = rol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
