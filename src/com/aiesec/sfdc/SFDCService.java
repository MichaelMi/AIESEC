package com.aiesec.sfdc;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SFDCService {

	public static void main(String[] args) {
		System.out.println(ConfigService.AliPayEmail);
		System.out.println(ConfigService.AliPayKey);
		System.out.println(ConfigService.AliPayName);
		System.out.println(ConfigService.AliPayPartner);
		System.out.println(ConfigService.SFEndPoint);
		System.out.println(ConfigService.SFPassWord);
		System.out.println(ConfigService.SFUserName);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMDD");
		System.out.println(df.format(Calendar.getInstance().getTime()));
		System.out.println(new Date().toLocaleString());
		System.out.println(new Date().getYear() + new Date().getMonth()
				+ new Date().getDay());
		DateFormat df1 = DateFormat.getDateInstance();// 日期格式，精确到日
		System.out.println(df1.format(date));
		DateFormat df2 = DateFormat.getDateTimeInstance();// 可以精确到时分秒
		System.out.println(df2.format(date));
		DateFormat df3 = DateFormat.getTimeInstance();// 只显示出时分秒
		System.out.println(df3.format(date));
	}
}
