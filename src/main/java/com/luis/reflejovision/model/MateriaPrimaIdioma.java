package com.luis.reflejovision.model;

public class MateriaPrimaIdioma {

	private Long idMateriaPrima;
	private String locale;
	private String nombre;
	
	
	public MateriaPrimaIdioma() {
		
	}


	public Long getIdMateriaPrima() {
		return idMateriaPrima;
	}


	public void setIdMateriaPrima(Long idMateriaPrima) {
		this.idMateriaPrima = idMateriaPrima;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Override
	public String toString() {
		return "MateriaPrimaIdioma [idMateriaPrima=" + idMateriaPrima + ", locale=" + locale + ", nombre=" + nombre
				+ "]";
	}
	
}
