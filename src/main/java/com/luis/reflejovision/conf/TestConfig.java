package com.luis.reflejovision.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestConfig {
	private static Logger logger = LogManager.getLogger(TestConfig.class);

	public TestConfig() {
		
	}

	public void testDBConfig() {
		String dbURL = ConfigurationParametersManager.getParameterValue("db.url");
	}

	public static void main(String[] args) {
		TestConfig test = new TestConfig();
		test.testDBConfig();

	}

}
