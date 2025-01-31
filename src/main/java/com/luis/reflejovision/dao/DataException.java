package com.luis.reflejovision.dao;

import com.luis.reflejovision.PinguelaException;

/**
 * Exception raíz de la capa de datos.
 */

public class DataException extends PinguelaException {

	public DataException() {
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}
}
