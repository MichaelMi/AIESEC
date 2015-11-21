package com.aiesec.sfdc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alipay.util.AlipayCore;
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
import com.sun.tools.classfile.Annotation.element_value;

public class SFDCService {
	EnterpriseConnection connection;
	// SFDCService初始化时实例化connection
	public SFDCService() {
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(ConfigService.SFUserName);
		config.setPassword(ConfigService.SFPassWord);
		config.setAuthEndpoint(ConfigService.SFEndPoint);
		config.setTraceMessage(true);
		try {
			connection = Connector.newConnection(config);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		System.out.println(connection);
		System.out.println("login success");
	}
	//将支付宝返回的数据处理并且同步到Salesforce
	public void ProcessAliPayDetailDataToSalesforce(String success_details, String fail_details) {
		String aliPayDetails = "";
		if (success_details != null) {
			aliPayDetails += success_details;
		}
		if (fail_details != null) {
			aliPayDetails += fail_details;
		}
		String[] details = aliPayDetails.split("\\|");
		List<Refund__c> reList = new ArrayList<Refund__c>();
		for (int i = 0; i < details.length; i++) {
			String[] record = details[i].split("\\^");
			Refund__c refund = new Refund__c();
			refund.setRefund_No__c(record[0]);
			if (record[4].equals("S")) {
				refund.setStatus__c("退款成功");
			} else {
				refund.setStatus__c("退款失败");
				refund.setFailure_Reason__c(record[5]);
			}
			refund.setAliPay_No__c(record[6]);
			refund.setAliPay_Complete_Time__c(record[7]);
			reList.add(refund);
			try {
				Refund__c[] reArray = reList.toArray(new Refund__c[reList.size()]);
				UpsertResult[] uResults = connection.upsert("Refund_No__c", reArray);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	// 更新批量退款状态为已处理
	public void UpdateRefundBatch(String batch_no, String success_details, String fail_details, String notify_time,
			String notify_id, String pay_user_id, String pay_user_name, String pay_account_no, String params) {
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
		rb.setLog_Result__c(params);
		try {
			UpsertResult[] uResult = connection.upsert("Batch_No__c", new Refund_Batch__c[] { rb });
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
	// 根据批次号查询当前批量退款是否处理过,处理过返回True，没处理过返回False
	public Boolean IsHandleBatch(String batch_no) {
		QueryResult results;
		try {
			String query = "select Batch_Status__c from Refund_Batch__c where Name = \'" + batch_no + "\'";
			results = connection.query(query);
			for (SObject sObj : results.getRecords()) {
				Refund_Batch__c rb = (Refund_Batch__c) sObj;
				if (rb.getBatch_Status__c().equals("已处理")) {
					return true;
				}
			}
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		return false;
	}
}