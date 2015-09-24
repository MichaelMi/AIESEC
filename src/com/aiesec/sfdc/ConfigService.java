package com.aiesec.sfdc;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ConfigService {

	public static String SFUserName;
	public static String SFPassWord;
	public static String SFEndPoint;
	public static String AliPayEmail;
	public static String AliPayName;;
	public static String AliPayPartner;
	public static String AliPayKey;
	public static String PayDate;
	public static String BatchNo;
	public static String TotalAmount;
	public static String TotalCount;
	public static String DetailData;

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

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String currentDateString = format.format(new Date());
			PayDate = currentDateString.replaceAll("-", "");
			BatchNo = PayDate + "00001";
			TotalAmount = "2";
			TotalCount = "2";
			DetailData = PayDate+"001^18500038131^陈立^1.0^退款1.0元|"+PayDate+"002^minilizai@sina.com^米立业^1.0^退款1.0元";

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
