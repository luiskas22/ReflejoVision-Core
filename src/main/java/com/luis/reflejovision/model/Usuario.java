package com.luis.reflejovision.model;

public class Usuario {
	private Long id;
	private String contrasena;
	private String username;
	private String nombre;
	private Long id_rol;
	private String correo;

	public Usuario() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getRol() {
		return id_rol;
	}

	public void setRol(Long id_rol) {
		this.id_rol = id_rol;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", username=" + username + ", nombre=" + nombre + ", rol=" + id_rol + ", correo="
				+ correo + "]";
	}

}
