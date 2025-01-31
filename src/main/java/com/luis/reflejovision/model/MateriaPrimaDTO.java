package com.luis.reflejovision.model;

import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaDTO {
	private String nombre;
	private Long id;
	private Double precio;
	private Integer unidades;
	private Long idUnidadMedida;
	
	private List<MateriaPrimaIdioma> traducciones;

	public MateriaPrimaDTO() {
		traducciones = new ArrayList<MateriaPrimaIdioma>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getUnidades() {
		return unidades;
	}

	public void setUnidades(Integer unidades) {
		this.unidades = unidades;
	}

	public Long getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(Long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}
	
	public List<MateriaPrimaIdioma> getTraducciones() {
		return traducciones;
	}

	public void setTraducciones(List<MateriaPrimaIdioma> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public String toString() {
		return "MateriaPrima [nombre=" + nombre + ", id=" + id + ", precio=" + precio + ", unidades=" + unidades
				+ ", idUnidadMedida=" + idUnidadMedida + "]";
	}

}
