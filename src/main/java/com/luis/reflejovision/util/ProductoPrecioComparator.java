package com.luis.reflejovision.util;

import java.util.Comparator;

import com.luis.reflejovision.model.Producto;

public class ProductoPrecioComparator implements Comparator<Producto>{

	public ProductoPrecioComparator() {

	}

	@Override
	public int compare(Producto one, Producto other) {
		if (one.getPrecio() < other.getPrecio()) {
			return -1;
		} else if (one.getPrecio() == other.getPrecio()) {
			return 0;
		} else {
			return 1;
		}

	}
}
