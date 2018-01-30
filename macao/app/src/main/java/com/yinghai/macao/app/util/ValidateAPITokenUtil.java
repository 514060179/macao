package com.yinghai.macao.app.util;

import com.yinghai.macao.app.model.TaxigoAccessTokens;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/5.
 */
public class ValidateAPITokenUtil {

    public static String encryKey = "2TatUM_JrpvYj2ubrWKN!qCmqGsn";


    public static Boolean ValidatingApiToken(String sendTimeString,String tokenString){
        String token = new ValidateAPITokenUtil().ctreatTokenStringByTimeString(sendTimeString);
        if(token.equals(tokenString)){
            return true;
        }
        return false;
    }
    /**
     *  生成签名
     * @param timeString
     * @return
     */
    private String  ctreatTokenStringByTimeString(String timeString) {
        String originString = timeString+encryKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArr = originString.getBytes();
            messageDigest.update(inputByteArr);
            byte[] resultByte = messageDigest.digest();
            String string  = byteArrToHex(resultByte);
            System.out.println("================"+string+"================");
            return string;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    private  String byteArrToHex(byte[] byteArr) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
        char[] resultCharArray =new char[byteArr.length * 2];
        int index = 0;
        for (byte b : byteArr) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        return new String(resultCharArray);
    }

    public static String getAccessTokenString(){
        //		accessTokenString = RandomUtil.getrandomString(64, typeEnum.both);
        return  UUID.randomUUID()+""+UUID.randomUUID();
    }

}
