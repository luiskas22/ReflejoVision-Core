package com.luis.reflejovision.service;

import com.luis.reflejovision.PinguelaException;

public class StockException extends PinguelaException{
	public StockException() {

	}

	public StockException(String message) {
		super(message);
	}

	public StockException(Throwable cause) {
		super(cause);
	}

	public StockException(String message, Throwable cause) {
		super(message, cause);
	}
}
