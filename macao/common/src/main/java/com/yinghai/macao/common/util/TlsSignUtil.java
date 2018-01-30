package com.yinghai.macao.common.util;

import com.tls.sigcheck.tls_sigcheck;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/2/15.
 */
public class TlsSignUtil {
    public final static String sdkAppId = PropertyUtil.getAppProperty("sdkAppId");
    public final static String accountType = PropertyUtil.getAppProperty("accountType");
    public final static String manager = PropertyUtil.getAppProperty("manager");
    public final static String jnisigcheckUrl = PropertyUtil.getAppProperty("jnisigcheckUrl");
    public final static String ec_key = PropertyUtil.getAppProperty("ec_key");
    public final static String public_key = PropertyUtil.getAppProperty("public_key");
    private static Logger log = Logger.getLogger(TlsSignUtil.class);
    public final static tls_sigcheck demo = new tls_sigcheck();

    static{
    	demo.loadJniLib(jnisigcheckUrl);
    }
    /**
     * tls后台签名
     * @param username tls用户名
     * @return
     * @throws Exception
     */
    public static String getTlsSignKey(String username) throws Exception {

//        tls_sigcheck demo = new tls_sigcheck();
        // 使用前请修改动态库的加载路径
        // demo.loadJniLib("D:\\src\\oicq64\\tinyid\\tls_sig_api\\windows\\64\\lib\\jni\\jnisigcheck.dll");
//        demo.loadJniLib(jnisigcheckUrl);
//        System.out.println("======"+TlsSignUtil.class.getClassLoader().getResource("").getPath());
        StringBuilder strBuilder = new StringBuilder();
        String s = "";
        //得到字节流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ec_key);
        //将字节流转化成字符流，并指定字符集
        InputStreamReader reader = new InputStreamReader(in,"UTF-8");
        BufferedReader br = new BufferedReader(reader);
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String priKey = strBuilder.toString();
        int ret = demo.tls_gen_signature_ex2(sdkAppId, username, priKey);

        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("sig:\n" + demo.getSig());
        }
        //得到字节流
        InputStream pubKeyFileIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(public_key);
        //将字节流转化成字符流，并指定字符集
        InputStreamReader pubKeyFile = new InputStreamReader(pubKeyFileIn,"UTF-8");
        br = new BufferedReader(pubKeyFile);
        strBuilder.setLength(0);
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String pubKey = strBuilder.toString();
        ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, sdkAppId, username);
       String sig = null;
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
            log.error("======获取TLS签名异常======"+demo.getErrMsg());
        }
        else
        {
            System.out.println("--\nverify ok -- expire time " + demo.getExpireTime() + " -- init time " + demo.getInitTime());
            sig = demo.getSig();
            log.info("======获取TLS签名成功======"+sig);
        }
        return sig;
    }
    public static void main(String[] orgs){
        try {
            System.out.println(getTlsSignKey("10867Passenger"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
