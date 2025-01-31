package com.luis.reflejovision.model;

public class ProductoCriteria {
	public static final String ORDER_BY_PRECIO = "PRECIO";
	public static final String ORDER_BY_NOMBRE = "NOMBRE";
	public static final String ORDER_BY_UNIDADES = "UNIDADES";
	public static final String ORDER_BY_ID = "ID";

	private Long id = null;

	private String nombre = null;

	private Double precioDesde = null;

	private Double precioHasta = null;

	private Integer unidadesDesde = null;

	private Integer unidadesHasta = null;

	private String locale = null;

	private String orderBy = ORDER_BY_ID;
	private Boolean ascDesc = Boolean.TRUE;

	public ProductoCriteria() {

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

	public Double getPrecioDesde() {
		return precioDesde;
	}

	public void setPrecioDesde(Double precioDesde) {
		this.precioDesde = precioDesde;
	}

	public Double getPrecioHasta() {
		return precioHasta;
	}

	public void setPrecioHasta(Double precioHasta) {
		this.precioHasta = precioHasta;
	}

	public Integer getUnidadesDesde() {
		return unidadesDesde;
	}

	public void setUnidadesDesde(Integer unidadesDesde) {
		this.unidadesDesde = unidadesDesde;
	}

	public Integer getUnidadesHasta() {
		return unidadesHasta;
	}

	public void setUnidadesHasta(Integer unidadesHasta) {
		this.unidadesHasta = unidadesHasta;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getAscDesc() {
		return ascDesc;
	}

	public void setAscDesc(Boolean ascDesc) {
		this.ascDesc = ascDesc;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public static String getOrderByPrecio() {
		return ORDER_BY_PRECIO;
	}

	public static String getOrderByNombre() {
		return ORDER_BY_NOMBRE;
	}

	public static String getOrderByUnidades() {
		return ORDER_BY_UNIDADES;
	}

	public static String getOrderById() {
		return ORDER_BY_ID;
	}

}
