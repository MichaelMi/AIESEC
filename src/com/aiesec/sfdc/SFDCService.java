package com.aiesec.sfdc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alipay.util.UtilDate;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.Refund_Batch__c;
import com.sforce.soap.enterprise.sobject.Refund__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFDCService {

	EnterpriseConnection connection;

	//SFDCService初始化时实例化connection
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
		System.out.println(connection);
		System.out.println("login success");
	}
	
	//将支付宝返回的成功数据同步到SFDC
	public void ProcessSuccessData(String success_details)
	{
		Refund__c[] reList =new Refund__c[]{};
		String[] details = success_details.split("|");
		for (int i = 0; i < details.length; i++) {
			String[] value = details[i].split("^");
			Refund__c refund = new Refund__c();
			refund.setRefund_No__c(value[0]);
			if (value[4] == "S") {
				refund.setStatus__c("退款成功");
			}
			refund.setAliPay_No__c(value[6]);
			refund.setAliPay_Complete_Time__c(value[7]);
			reList[i] = refund;
		}
		try {
			UpsertResult[] uResults = connection.upsert("Refund_No__c", reList);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//将支付宝返回的失败数据同步到SFDC
	public void ProcessFailData(String fail_details) {
		Refund__c[] reList =new Refund__c[]{};
		String[] details = fail_details.split("|");
		for (int i = 0; i < details.length; i++) {
			String[] value = details[i].split("^");
			Refund__c refund = new Refund__c();
			refund.setRefund_No__c(value[0]);
			if (value[4] == "F") {
				refund.setStatus__c("退款失败");
				refund.setFailure_Reason__c(value[5]);
			}
			refund.setAliPay_No__c(value[6]);
			refund.setAliPay_Complete_Time__c(value[7]);
			reList[i] = refund;
		}
		try {
			UpsertResult[] uResults = connection.upsert("Refund_No__c", reList);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//更新批量退款状态为已处理
	public void UpdateRefundBatch(String batch_no,String success_details,String fail_details,String notify_time,String notify_id,String pay_user_id,String pay_user_name,String pay_account_no)
	{
		Refund_Batch__c rb = new Refund_Batch__c();
		rb.setBatch_No__c(batch_no);
		rb.setSuccess_Details_Data__c(success_details);
		rb.setFail_Details_Data__c(fail_details);
		rb.setAliPay_Notify_ID__c(notify_id);
		rb.setAliPay_Notify_Time__c(notify_time);
		rb.setBatch_Status__c("已处理");
		rb.setPay_Account_No__c(pay_account_no);
		rb.setPay_User_ID__c(pay_user_id);
		rb.setPay_User_Name__c(pay_user_name);
		try {
			UpsertResult[] uResult = connection.upsert("Batch_No__c", new Refund_Batch__c[]{rb});
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//根据批次号查询当前批量退款是否处理过
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
	//记录日志
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
