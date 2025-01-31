package com.luis.reflejovision.service;

import java.util.List;

public interface MailService {

	public void enviar(String para, String asunto, String msg) throws MailException;

	public void enviar(List<String> para, String asunto, String msg);

}
