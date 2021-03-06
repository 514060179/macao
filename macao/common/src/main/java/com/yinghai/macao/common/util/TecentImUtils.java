package com.yinghai.macao.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yinghai.macao.common.constant.Constant;
import com.yinghai.macao.common.constant.SpcarDriverPushCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TecentImUtils {
	
 // 用户身份
	private static final String identifier = PropertyUtil.getAppProperty("identifier");
 // 应用id	
	private static final String sdkAppId = PropertyUtil.getAppProperty("sdkAppId");
	
	public static void connect() throws Exception{
	
		File usigfile = new File("E:\\tools\\sig"); 
		BufferedReader br = new BufferedReader(new FileReader(usigfile));
		StringBuffer usersig = new StringBuffer();
		String temp = null;
		while ((temp=br.readLine())!=null) {
			usersig.append(temp);
		}
		br.close();
		System.out.println(usersig.toString());
		String mark = boardcast(usersig.toString(), "Hello World");
		System.out.println(mark);
	}
	
	/**
	 * 
	 * @param usersig 签名
	 * @param putText 推送消息
	 * @return 推送结果的报告
	 * @throws Exception
	 * 
	 * 向全体用户推送消息
	 */
	public static String boardcast(String usersig,String putText) throws Exception{
		URL url =new URL("https://console.tim.qq.com/v4/openim/im_push?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		int msgRandmon = Integer.parseInt(msgrandom());
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonObject.put("MsgRandom",msgRandmon);
		JSONObject text = new JSONObject();
		text.put("Text", putText);
		JSONObject content = new JSONObject();
		content.put("MsgType", "TIMTextElem");
		content.put("MsgContent", text);
		jsonArray.add(content);
		jsonObject.put("MsgBody", jsonArray);
		System.out.println(jsonObject.toString());
		OutputStreamWriter outPut = new OutputStreamWriter(connection.getOutputStream());
		outPut.write(jsonObject.toString());
		outPut.flush();
		outPut.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(input);
		StringBuffer sb = new StringBuffer();
		String temp2 = null;
		while ((temp2=bufferedReader.readLine())!=null) {
			sb.append(temp2);
			
		}
		input.close();
		JSONObject jsonObject2 = JSONObject.fromObject(sb.toString());
		System.out.println(jsonObject2.toString());
		String status = (String)jsonObject2.get("ActionStatus");
		System.out.println(status);
		if (status.equals("OK")) {
			String taskId = jsonObject2.getString("TaskId");
			return getReport(usersig,taskId);
		}
		else {
			return "fail";
			
		}
	}
	/**
	 * 
	 * @param usersig
	 * @param id
	 * @return 返回推送结果的报告
	 * @throws Exception
	 * 
	 * 单推
	 */
	public static String pushOne(String usersig,String id,String putText) throws Exception{
		URL url = new URL("https://console.tim.qq.com/v4/openim/im_push?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		Long msgrandom = Long.parseLong(msgrandom());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("MsgRandom", msgrandom);
		JSONArray tags = new JSONArray();
		JSONArray body = new JSONArray();
		JSONObject content = new JSONObject();
		JSONObject condition = new JSONObject();
		JSONObject text = new JSONObject();
		tags.add(id);
		condition.put("TagsAnd", tags);
		jsonObject.put("Condition", condition);
		text.put("Text", putText);
		content.put("MsgType", "TIMTextElem");
		content.put("MsgContent",text);
		body.add(content);
		jsonObject.put("MsgBody", body);
		System.out.println(jsonObject.toString());
		OutputStreamWriter output  = new OutputStreamWriter(connection.getOutputStream());
		output.write(jsonObject.toString());
		output.flush();
		output.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
			
		}
		JSONObject jsonObject2 = JSONObject.fromObject(buffer.toString());
		return jsonObject2.toString();
	}
	/**
	 * 
	 * @param usersig
	 * @return
	 * @throws Exception
	 * 
	 * 给关注 xx 或 xx 标签的用户推送消息
	 */
	public static String tagsOrPush(String usersig) throws Exception{

		int msgRandmon = Integer.parseInt(msgrandom());
		String json = "";
		return getReport(usersig, " ");
	}
	/**
	 * 
	 * @param usersig
	 * @return
	 * @throws Exception
	 * 
	 * 给关注 xx 和 xx 标签的用户推送消息
	 */
	public static String tagsAndPush(String usersig) throws Exception {
	
		int msgRandmon = Integer.parseInt(msgrandom());
		String json="";
		return getReport(usersig, " ");
		
	}
	/**
	 * 
	 * @return
	 * 
	 * 给有 xx 属性 或 xx 属性的用户推送消息
	 */
	public static String attrsOrPush(String usersig) throws Exception {

		int msgRandmon = Integer.parseInt(msgrandom());
		String json="";
		return getReport(usersig, " ");
	}
	/**
	 * 
	 * @return
	 * 
	 * 给有 xx 属性 和 xx 属性的用户推送消息
	 */
	public static String attrsAndPush(String usersig) throws Exception{

		int msgRandmon = Integer.parseInt(msgrandom());
		String json = "";
		return getReport(usersig, " ");
	}

	/**
	 * 
	 * @param usersig 签名
	 * @param taskId 推送任务id
	 * @return 推送结果的报告
	 * @throws Exception
	 * 
	 * 获取推送结果
	 */
	public static String getReport(String usersig,String taskId) throws Exception {
		String reportStr = "{\"TaskIds\": [\""+taskId+"\"]}";
		JSONObject reportJson = JSONObject.fromObject(reportStr);
		URL url = new URL("https://console.tim.qq.com/v4/openim/im_get_push_report?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter outPut = new OutputStreamWriter(connection.getOutputStream());
		outPut.write(reportJson.toString());
		outPut.flush();
		outPut.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
		}
		input.close();
		JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
		String status = jsonObject.getString("ActionStatus");
		if (status.equals("OK")) {
			return jsonObject.toString();
		}
		else {
			return "get report fail";
		}
	}
	/**
	 * 
	 * @param usersig 签名
	 * @param userid 用户id
	 * @return 登录是否成功的信息
	 * @throws Exception
	 * 
	 * 导入账号
	 */
	public static String login(String usersig,String userid,String username) throws Exception{
		URL url =new URL("https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Identifier", userid);
		if(!StringUtil.empty(username)){
			jsonObject.put("Nick", username);
		}
		System.out.println(jsonObject.toString());
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(jsonObject.toString());
		output.flush();
		output.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
		}
		input.close();
		JSONObject jsonObject2 = JSONObject.fromObject(buffer.toString());
		return jsonObject2.toString();
	}
	/**
	 * 
	 * @param usersig 签名
	 * @param userid 用户id
	 * @return 返回添加是否成功的信息
	 * @throws Exception
	 * 
	 * 添加用户标签
	 */
	public static String addTags(String usersig,String userid,JSONArray tags) throws Exception{
		URL url = new URL("https://console.tim.qq.com/v4/openim/im_add_tag?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject content = new JSONObject();
//		JSONArray tags = new JSONArray();
//		tags.add(userid);
		content.put("To_Account",userid);
		content.put("Tags", tags);
		jsonArray.add(content);
		jsonObject.put("UserTags", jsonArray);
		System.out.println(jsonObject.toString());
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(jsonObject.toString());
		output.flush();
		output.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
		}
		JSONObject jsonObject2 = JSONObject.fromObject(buffer.toString());
		return jsonObject2.toString();
	}
	/**
	 * 
	 * @param usersig 签名
	 * @param userid 用户id
	 * @return 返回用户的拥有的所有标签信息
	 * @throws Exception
	 * 
	 * 获取用户标签
	 */
	public static String getTag(String usersig,String userid) throws Exception{
		URL url =new URL("https://console.tim.qq.com/v4/openim/im_get_tag?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(userid);
		jsonObject.put("To_Account", jsonArray);
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(jsonObject.toString());
		output.flush();
		output.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
		}
		JSONObject jsonObject2 = JSONObject.fromObject(buffer.toString());
		return jsonObject2.toString();
	}
	/**
	 * 
	 * @param usersig 签名
	 * @param userid 用户id
	 * @return 返回用户的在线状态
	 * @throws Exception
	 * 
	 * 获取用户的在线状态
	 */
	public static String querystate(String usersig,String userid)throws Exception{
		URL url =new URL("https://console.tim.qq.com/v4/openim/querystate?usersig="+usersig+"&identifier="+identifier+"&sdkAppId="+sdkAppId+"&random="+random()+"&contenttype=json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(userid);
		jsonObject.put("To_Account", jsonArray);
		System.out.println(jsonObject.toString());
		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
		output.write(jsonObject.toString());
		output.flush();
		output.close();
		InputStreamReader input = new InputStreamReader(connection.getInputStream());
		BufferedReader bf = new BufferedReader(input);
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while ((temp=bf.readLine())!=null) {
			buffer.append(temp);
		}
		input.close();
		JSONObject jsonObject2 = JSONObject.fromObject(buffer.toString());
		return jsonObject2.toString();
	}
	/**
	 * 
	 * @return
	 * 
	 * 生成随机调用接口用的随机数
	 */
	public static String random(){
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < 32; j++) {
			int temp = (int)(Math.random()*8)+1;
			sb.append(temp);
		}
		return sb.toString();
	}
	/**
	 * 
	 * @return
	 * 
	 * 用现在时间生成推送号
	 */
	public static String msgrandom() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String msgRadom = sdf.format(date).toString();
		int a = (int)(Math.random()*(9999-1000+1))+1000;
		return msgRadom+a;
	}

	public static void main(String[] args) throws Exception {
		String sign = TlsSignUtil.getTlsSignKey("admin");
		com.alibaba.fastjson.JSONObject responseObject = new com.alibaba.fastjson.JSONObject();
		responseObject.put("msg", "success");
		responseObject.put("code", SpcarDriverPushCode.PUSHBYTAGS);
		responseObject.put("data", "ss看看本地1111");
//		System.out.println(TecentImUtils.login(sign,"android03"));//IOStest1  service01 IOStest01
//		System.out.println(pushOne(sign, Constant.driver,responseObject.toJSONString()+""));
		System.out.println(pushOne(sign, Constant.driver,"\"code\":\"901\",\"data\":\"9999問問ss看看本地1111\",\"msg\":\"success\""));
//		System.out.println(pushOne(sign, Constant.driver,"11哈哈哈哈"));
	}
}
