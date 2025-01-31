package com.luis.reflejovision.model;

public class UnidadMedida {
	private Long id;
	private String nombre;

	public UnidadMedida() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "UnidadMedida [id=" + id + ", unidadesMedida=" + nombre + "]";
	}

}
