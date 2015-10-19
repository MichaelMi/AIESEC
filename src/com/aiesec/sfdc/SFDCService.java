package com.aiesec.sfdc;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.alipay.util.UtilDate;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Refund_Batch__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFDCService {

	EnterpriseConnection connection;

	public SFDCService() {
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(ConfigService.SFUserName);
		config.setPassword(ConfigService.SFPassWord);
		config.setAuthEndpoint(ConfigService.SFEndPoint);
		config.setTraceMessage(true);
		try {
			connection = Connector.newConnection(config);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Boolean IsHandleBatch(String batch_no){
		QueryResult results;
		try {
			results = connection
					.query("select Batch_Status__c from Refund_Batch__c where Name = "
							+ batch_no);
			for (SObject sObj : results.getRecords()) {
				Refund_Batch__c rb = (Refund_Batch__c) sObj;
				if (rb.getBatch_Status__c() == "已处理") {
					return true;
				}
			}
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void LogData(String data) 
	{
		Refund_Batch__c rb = new Refund_Batch__c();
		rb.setDetail_Data__c(data);
		try {
			SaveResult[] sr = connection.create(new SObject[]{rb});
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(ConfigService.AliPayEmail);
		System.out.println(ConfigService.AliPayKey);
		System.out.println(ConfigService.AliPayName);
		System.out.println(ConfigService.AliPayPartner);
		System.out.println(ConfigService.SFEndPoint);
		System.out.println(ConfigService.SFPassWord);
		System.out.println(ConfigService.SFUserName);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateString = format.format(new Date());
		System.out.println(currentDateString.replaceAll("-", "") + "sssssss");

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
		Date date2 = new Date();
		date2.getYear();
		date2.getDate();
		System.out.println(date2.getDate());
		
		System.out.println(UtilDate.getDate());
		System.out.println(UtilDate.getDateFormatter());
		System.out.println(UtilDate.getOrderNum());
		System.out.println(UtilDate.getThree());
		
		SFDCService sfdcService = null;
		sfdcService = new SFDCService();
		sfdcService.LogData("sssssssssssss");
				
	}
}
