package com.luis.reflejovision.model;

import java.util.ArrayList;
import java.util.List;

public class Producto {
	private Long id;
	private String nombre;
	private Double precio;
	private Integer unidades;

	private List<ConsumoDTO> consumos = null;

	public Producto() {
		consumos = new ArrayList<ConsumoDTO>();
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

	public List<ConsumoDTO> getConsumos() {
		return consumos;
	}

	public void setConsumos(List<ConsumoDTO> consumos) {
		this.consumos = consumos;
	}

	@Override
	public String toString() {
		String s = "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", unidades=" + unidades + "]";
		for (ConsumoDTO c : consumos) {
			s += c;
		}
		return s;

	}
}
