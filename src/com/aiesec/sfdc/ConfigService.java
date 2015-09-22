package com.aiesec.sfdc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {

	public static String SFUserName;
	public static String SFPassWord;
	public static String SFEndPoint;
	public static String AliPayEmail;
	public static String AliPayName;;
	public static String AliPayPartner;
	public static String AliPayKey;

	static {
		Properties p = new Properties();
		InputStream inputStream = ConfigService.class
				.getResourceAsStream("/com/aiesec/sfdc/config.properties");
		try {
			p.load(inputStream);
			inputStream.close();
			SFUserName = p.getProperty("SFUserName");
			SFPassWord = p.getProperty("SFPassWord");
			SFEndPoint = p.getProperty("SFEndPoint");
			AliPayEmail = p.getProperty("AliPayEmail");
			AliPayName = p.getProperty("AliPayName");
			AliPayPartner = p.getProperty("AliPayPartner");
			AliPayKey = p.getProperty("AliPayKey");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
