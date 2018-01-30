package com.yinghai.macao.common.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;


/**
 * 
 * <strong>discrition:</strong>类型转换
 *
 */
@SuppressWarnings({"rawtypes" , "unchecked"})
public class TransformUtils {

	public static boolean toBoolean(Object obj) {

		return toBoolean(obj, false);
	}

	public static boolean toBoolean(Object obj, boolean defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(toString(obj));
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static byte toByte(Object obj) {
		return toByte(obj, (byte) 0);
	}

	public static byte toByte(Object obj, byte defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.byteValue();
		}
		String value = toString(obj) ;
		try {
			return Byte.parseByte( value ) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static char toCharacter(Object obj) {
		return toCharacter(obj, (char) ' ');
	}

	public static char toCharacter(Object obj, char defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		String str = obj.toString().trim();
		if (str.length() == 0) {
			return defaultValue;
		}
		return (char) str.toCharArray()[0];
	}

	public static double toDouble(Object obj) {
		return toDouble(obj, 0d);
	}

	public static double toDouble(Object obj, double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.doubleValue() ;
		}
		String value = toString(obj) ;
		try {
			return Double.parseDouble(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static float toFloat(Object obj) {
		return toFloat(obj, 0);
	}

	public static float toFloat(Object obj, float defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.floatValue();
		}
		String value = toString(obj) ;
		try {
			return Float.parseFloat(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static int toInt(Object obj) {
		return toInt(obj, 0);
	}

	public static int toInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.intValue();
		}
		String value = toString(obj) ;
		try {
			return Integer.parseInt(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static long toLong(Object obj) {
		return toLong(obj, 0L);
	}

	public static long toLong(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.longValue();
		}
		String value = toString(obj) ;
		try {
			return Long.parseLong(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static short toShort(Object obj) {
		return toShort(obj, (byte) 0);
	}
	
	
	public static short toShort(Object obj, short defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.shortValue();
		}
		String value = toString(obj) ;
		try {
			return Short.parseShort(value) ;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String toString(Object value) {
		if (value == null) {
			return "";
		}
		if(value instanceof BigDecimal){
			BigDecimal bigDecimal = (BigDecimal) value ;
			DecimalFormat df = new DecimalFormat("##########################.##############");
			return df.format(bigDecimal) ;
		}
		
		if(value instanceof Number){
			Number number = (Number) value ;
			return String.valueOf( number ) ;
		}
		return value.toString().trim();
	}
	
	
	public static BigDecimal toBigDecimal(Object value){
		return toBigDecimal(value , new BigDecimal(0)) ;
	}
	
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if(value == null){
			return defaultValue ;
		}
		if(value instanceof BigDecimal){
			BigDecimal decimal = (BigDecimal) value ;
			return decimal; 
		}
		return new BigDecimal( toDouble( value ) );
	}

	public static String dateToString(Object value, String pattern){
		Date date = (Date) value;
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime( date ) ; 
        SimpleDateFormat sdf = new SimpleDateFormat( pattern );  
        return sdf.format( date ) ; 
	}


	
	public static boolean isNull(Object value){
		if(value == null){
			return true ; 
		}
		if(value.getClass().isArray()){
			if(Array.getLength(value) == 0){
				return true ; 
			}
		}
		if(value instanceof Collection<?>){
			Collection<?> collection = (Collection<?>) value ;
			if(collection.isEmpty()){
				return true ;
			}
		}else if(value instanceof Map<?, ?>){
			Map<?, ?> map = (Map<?, ?>) value ;
			if(map.isEmpty()){
				return true ;
			}
		}else if(value instanceof String){
			String string = (String) value ;
			return isNull(string) ;
		}
		return false; 
	}
	
	public static boolean isNull(String value){
		if(value == null){
			return true ;
		}
		if("".equals(value.trim())){
			return true ;
		}
		return false ;
	}
	

	
	public static boolean exists(String[]array , String item){
		if(TransformUtils.isNull(array)){
			return false ;
		}
		item = TransformUtils.toString( item ) ;
		for(String value : array){
			if(item.equals(value)){
				return true ; 
			}
		}
		return false ;
	}
	

	
	public static String getBasepath(HttpServletRequest request)
	  {
	    String path = request.getContextPath();
	    String basePath = request.getScheme() + 
	      "://" + 
	      request.getServerName() + (
	      request.getServerPort() == 80 ? "" : new StringBuilder(":").append(
	      request.getServerPort()).toString()) + 
	      path + "/";

	    return basePath;
	  }
	

	

	
	public static String randStr(int len){
		StringBuffer buffer =  new StringBuffer();
		for(int x=65;x<=90;x++){
			buffer.append( (char)x );
		}
		int strLenth = buffer.length() ;
		StringBuffer randStr = new StringBuffer();
		for(int x=0;x<len;x++){
			randStr.append( buffer.charAt(new Random().nextInt( strLenth ))) ;
		}
		return randStr.toString() ;
	}
	
	public static String replaceToBr(String str){
		if(str==null){
			return "";
		}
		//str = str.replaceAll("\n", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");	407bug
		str = str.replaceAll("\n", "<br/>");
		//str = str.replaceAll("\t", "<br/>");
        //str = str.replaceAll("\r", "<br/>");
        return str;
	}
	
}
