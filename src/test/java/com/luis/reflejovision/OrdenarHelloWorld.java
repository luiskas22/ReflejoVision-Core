package com.luis.reflejovision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.util.ProductoIdComparator;
import com.luis.reflejovision.util.ProductoPrecioComparator;

public class OrdenarHelloWorld {

	public static void main(String[] args) {
		List<Producto> productos = new ArrayList<Producto>();
		Producto p = null;
		p = new Producto();
		p.setId(1l);
		p.setNombre("Espejo 1");
		p.setPrecio(2.00);
		p.setUnidades(5);
		productos.add(p);
		
		p = new Producto();
		p.setId(5l);
		p.setNombre("Espejo 5");
		p.setPrecio(8.00);
		p.setUnidades(9);
		productos.add(p);
		
		p = new Producto();
		p.setId(2l);
		p.setNombre("Espejo 2");
		p.setPrecio(5.00);
		p.setUnidades(2);
		productos.add(p);
		System.out.println("Antes de sort:");
		for (Producto pr : productos) {
			System.out.println(pr);
		}
		Collections.sort(productos, new ProductoPrecioComparator());
		System.out.println("Despues de sort:");
		for (Producto pr : productos) {
			System.out.println(pr);
		}
	}

}
