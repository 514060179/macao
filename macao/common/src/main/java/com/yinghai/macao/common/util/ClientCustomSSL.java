package com.yinghai.macao.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.yinghai.macao.common.constant.Constant;


public class ClientCustomSSL {

	public static String doRefund(String url, String data,String  payType) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String mchid = "";
		FileInputStream is = null;
		//为APP微信退款
		if(StringUtil.notEmpty(payType)&&payType.equals(Constant.weixinAPPPayType)){
			is = new FileInputStream(new File(Constant.apiclientCert));
			mchid = Constant.mchId;
		}else if(StringUtil.notEmpty(payType)&&payType.equals(Constant.weixinJSAPPayType)){//微信公众号退款
			is = new FileInputStream(new File(Constant.apiclientCertJsapi));
			mchid = Constant.JSAPImchId;
		}else{
			is = new FileInputStream(new File(Constant.apiclientCertJsapi2));
			mchid = Constant.JSAPImchId2;
		}
		keyStore.load(is, mchid.toCharArray());// 这里写密码..默认是你的MCHID

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
		.loadKeyMaterial(keyStore, mchid.toCharArray())// 这里也是写密码的
		.build();
		  // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

            //HttpGet httpget = new HttpGet("https://api.mch.weixin.qq.com/secapi/pay/refund");
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httppost.setEntity(new StringEntity(data, "UTF-8"));
            System.out.println("executing request" + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(response.getEntity(),
                		"UTF-8");
                		EntityUtils.consume(entity);
                		return jsonStr;
	}

}
