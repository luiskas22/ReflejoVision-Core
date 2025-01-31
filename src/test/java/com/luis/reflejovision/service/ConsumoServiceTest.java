package com.luis.reflejovision.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.dao.DataException;
import com.luis.reflejovision.model.ConsumoDTO;
import com.luis.reflejovision.model.Producto;
import com.luis.reflejovision.service.impl.ConsumoServiceImpl;
import com.luis.reflejovision.service.impl.ProductoServiceImpl;

public class ConsumoServiceTest {
	private ConsumoService consumoService = null;
	private static Logger logger = LogManager.getLogger(ConsumoServiceTest.class);
	private ProductoService productoService = null;

	public ConsumoServiceTest() {
		consumoService = new ConsumoServiceImpl();
		productoService = new ProductoServiceImpl();
	}

	public void testFindByProducto() throws Exception{
		List<ConsumoDTO> consumos = consumoService.findByProducto(16l);;

		System.out.println(consumos);
	}
	
	public void testCreateConsumos() throws DataException {
		logger.traceEntry("Testing create consumos");

		Long idProducto = 36L;
		List<ConsumoDTO> consumos = new ArrayList<ConsumoDTO>();
		ConsumoDTO c = null;
		c = new ConsumoDTO();
		c.setIdMateriaPrima(27L);
		c.setUnidades(2.0);
		consumos.add(c);

		c = new ConsumoDTO();
		c.setIdMateriaPrima(28L);
		c.setUnidades(2.0);
		consumos.add(c);

		Producto producto = productoService.findById(idProducto);
		if (producto != null) {
			try {
				consumoService.create(idProducto, consumos);
				logger.trace("Consumos creados exitosamente para el producto con ID " + idProducto);
			} catch (DataException e) {
				logger.error("Error al crear los consumos para el producto con ID " + idProducto, e);
				throw e;
			}
		} else {
			logger.trace("No se pudo crear los consumos porque el producto con ID " + idProducto + " no existe");
		}
	}

	public void testCreate() throws DataException {
		logger.traceEntry("Testing create");
		ConsumoDTO c = new ConsumoDTO();
		c.setIdProducto(10l);
		c.setIdMateriaPrima(17l);
		c.setUnidades(2.00);
		
		Producto producto = productoService.findById(c.getIdProducto());
		if (producto != null) {
			consumoService.create(c);
			logger.trace("Guardado " + c);
		} else {
			logger.trace("No se pudo crear el consumo porque el producto con ID " + c.getIdProducto() + " no existe");
		}
	}

	public void testDelete() throws DataException {
		logger.traceEntry("Testing delete...");
		ConsumoDTO consumo = new ConsumoDTO();
		consumo.setIdProducto(10l);
		consumo.setIdMateriaPrima(19l);

		Producto producto = productoService.findById(consumo.getIdProducto());

		if (producto != null) {
			consumoService.delete(consumo.getIdProducto(), consumo.getIdMateriaPrima());
			logger.trace("Borrado " + consumo);
		} else {
			logger.trace(
					"No se pudo borrar el consumo porque el producto con ID " + consumo.getIdProducto() + " no existe");
		}
	}

	public void testUpdate() throws DataException {
		logger.traceEntry("Testing update...");
		ConsumoDTO c = new ConsumoDTO();
		c.setIdProducto(10l);
		c.setIdMateriaPrima(17l);
		c.setUnidades(1.00);

		Producto producto = productoService.findById(c.getIdProducto());
		if (producto != null) {
			consumoService.update(c);
			logger.trace("Actualizado " + c);
		} else {
			logger.trace(
					"No se pudo actualizar el consumo porque el producto con ID " + c.getIdProducto() + " no existe");
		}
	}

	public void testDeleteByProducto() throws DataException {
		logger.traceEntry("Testing deleteByProducto...");
		ConsumoDTO consumo = new ConsumoDTO();
		consumo.setIdProducto(24l);

		Producto producto = productoService.findById(consumo.getIdProducto());

		if (producto != null) {
			consumoService.deleteByProducto(consumo.getIdProducto());
			logger.trace("Borrado " + consumo);
		} else {
			logger.trace(
					"No se pudo borrar el consumo porque el producto con ID " + consumo.getIdProducto() + " no existe");
		}
	}

	public static void main(String[] args) throws Exception {
		ConsumoServiceTest test = new ConsumoServiceTest();
		test.testFindByProducto();
//		test.testCreateConsumos();
//		test.testCreate();
//		test.testDelete();
//		test.testUpdate();
//		test.testDeleteByProducto();
	}

}
