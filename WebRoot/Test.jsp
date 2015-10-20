<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.aiesec.sfdc.*"%>
<%@ page import="com.sforce.ws.*" %>
<%
//获取支付宝POST过来反馈信息
	Map<String, String> params = new HashMap<String, String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter
			.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	
	//批量付款数据中转账成功的详细信息
	SFDCService sfdcService = new SFDCService();
	String success_details = new String(request.getParameter(
			"success_details").getBytes("ISO-8859-1"), "UTF-8");
	sfdcService.LogData(success_details);
	//批量付款数据中转账失败的详细信息
		String fail_details = new String(request.getParameter(
				"fail_details").getBytes("ISO-8859-1"), "UTF-8");
		sfdcService.LogData(fail_details);
		//拿到返回的批次号
		String batch_no = new String(request.getParameter("batch_no")
				.getBytes("ISO-8859-1"), "UTF-8");
		sfdcService.LogData(batch_no);
		//拿到所有参数
		Map<String, String[]> parameters = request.getParameterMap();
		sfdcService.LogData(parameters.toString());
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

	if (AlipayNotify.verify(params)) {//验证成功
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码

		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

		//判断是否在商户网站中已经做过了这次通知返回的处理

		//Boolean isHandleBatch = sfdcService.IsHandleBatch(batch_no);
		//if (!isHandleBatch) {

		//}
		//如果没有做过处理，那么执行商户的业务程序
		//如果有做过处理，那么不执行商户的业务程序

		out.println("success"); //请不要修改或删除

		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

		//////////////////////////////////////////////////////////////////////////////////////////
	} else {//验证失败
		out.println("fail");
	}
%>