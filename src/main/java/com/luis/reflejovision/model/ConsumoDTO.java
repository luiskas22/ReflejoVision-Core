package com.luis.reflejovision.model;

import java.util.List;

public class ConsumoDTO {

	private Long idProducto;
	private Long idMateriaPrima;
	private String nombreMateriaPrima;
	private Integer idUnidadMedidaMp;
	private Double precioMateriaPrima;
	private Double unidades;
	

	public ConsumoDTO() {

	}

	public Long getIdMateriaPrima() {
		return idMateriaPrima;
	}

	public void setIdMateriaPrima(Long idMateriaPrima) {
		this.idMateriaPrima = idMateriaPrima;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Double getUnidades() {
		return unidades;
	}

	public void setUnidades(Double unidades) {
		this.unidades = unidades;
	}


	public String getNombreMateriaPrima() {
		return nombreMateriaPrima;
	}

	public void setNombreMateriaPrima(String nombreMateriaPrima) {
		this.nombreMateriaPrima = nombreMateriaPrima;
	}

	public Integer getIdUnidadMedidaMp() {
		return idUnidadMedidaMp;
	}

	public void setIdUnidadMedidaMp(Integer idUnidadMedidaMp) {
		this.idUnidadMedidaMp = idUnidadMedidaMp;
	}

	public Double getPrecioMateriaPrima() {
		return precioMateriaPrima;
	}

	public void setPrecioMateriaPrima(Double precioMateriaPrima) {
		this.precioMateriaPrima = precioMateriaPrima;
	}

	@Override
	public String toString() {
		return "ConsumoDTO [idProducto=" + idProducto + ", idMateriaPrima=" + idMateriaPrima + ", nombreMateriaPrima="
				+ nombreMateriaPrima + ", unidades=" + unidades + ", idUnidadMedidaMp=" + idUnidadMedidaMp
				+ ", precioMateriaPrima=" + precioMateriaPrima + "]";
	}


	
}