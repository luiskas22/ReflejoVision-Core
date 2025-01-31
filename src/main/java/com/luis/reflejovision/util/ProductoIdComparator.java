package com.luis.reflejovision.util;

import java.util.Comparator;

import com.luis.reflejovision.model.Producto;

public class ProductoIdComparator implements Comparator<Producto> {

	public ProductoIdComparator() {

	}

	@Override
	public int compare(Producto one, Producto other) {
		if (one.getId() < other.getId()) {
			return -1;
		} else if (one.getId() == other.getId()) {
			return 0;
		} else {
			return 1;
		}

	}

}
