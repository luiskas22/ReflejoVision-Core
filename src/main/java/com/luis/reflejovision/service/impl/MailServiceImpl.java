package com.luis.reflejovision.service.impl;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luis.reflejovision.service.MailException;
import com.luis.reflejovision.service.MailService;

public class MailServiceImpl implements MailService {
	private static Logger logger = LogManager.getLogger(MailServiceImpl.class);
	private static String SERVER_NAME = "smtp-mail.outlook.com";
	private static int SERVER_PORT = 587;
	private static String USER = "reflejovisionoficial@outlook.es";
	private static String PASSWORD = "Predator22";
	
	public MailServiceImpl() {

	}

	public void enviar(String para, String asunto, String msg) throws MailException {

		try {
			Email email = new SimpleEmail();
			email.setHostName(SERVER_NAME);
			email.setSmtpPort(SERVER_PORT);
			email.setAuthenticator(new DefaultAuthenticator(USER, PASSWORD));
			// email.setSSLOnConnect(true);
			email.setStartTLSEnabled(true);
			email.setFrom(USER);
			email.setSubject(asunto);
			email.setMsg(msg);
			email.addTo(para);
			email.send();

		} catch (EmailException e) {
			logger.info("Enviando email desde " + USER + " para " + para + ":");
			throw new MailException("Enviando email a " + para, e);
		}
	}

	@Override
	public void enviar(List<String> para, String asunto, String msg) {
		// TODO Auto-generated method stub

	}

}
