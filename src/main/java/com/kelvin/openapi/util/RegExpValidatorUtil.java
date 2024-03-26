/*
 * Copyright  2017. qooco.com All Rights Reserved. 
 * Application : qoocoAPI 
 * Class Name  : RegExpValidatorUtil.java
 * Date Created: 2017年1月19日
 * Author      : Kelvin范显
 * 
 */
package com.kelvin.openapi.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author Kelvin范显
 * @createDate 2017年1月19日
 */
public class RegExpValidatorUtil {
    /**
     * 验证邮箱
     * 
     * @param str
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String str) {
    
        String regex = "^([\\w-\\.]+)@((";
        return match(regex, str);
    }
    
    /**
     * 验证IP地址
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIP(String str) {
    
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        return match(regex, str);
    }
    
    /**
     * 验证网址Url
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUrl(String str) {
    
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }
    
    /**
     * 验证电话号码
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsTelephone(String str) {
    
        String regex = "^(";
        return match(regex, str);
    }
    
    /**
     * 验证输入密码条件(字符与数据同时出现)
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPassword(String str) {
    
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }
    
    /**
     * 验证输入密码长度 (6-18位)
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPasswLength(String str) {
    
        String regex = "^\\d{6,18}$";
        return match(regex, str);
    }
    
    /**
     * 验证输入邮政编号
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsPostalcode(String str) {
    
        String regex = "^\\d{6}$";
        return match(regex, str);
    }
    
    /**
     * 验证输入手机号码
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsHandset(String str) {
    
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }
    
    /**
     * 验证输入身份证号
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIDcard(String str) {
    
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        return match(regex, str);
    }
    
    /**
     * 验证输入两位小数
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDecimal(String str) {
    
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }
    
    /**
     * 验证输入一年的12个月
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsMonth(String str) {
    
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }
    
    /**
     * 验证输入一个月的31天
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsDay(String str) {
    
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }
    
    /**
     * 验证日期格式 格式为"yyyy-MM-dd"
     * 
     * @method: RegExpValidatorUtil  isDate
     * @param str str
     * @return  boolean
     * @createDate： 2014年10月22日
     * @2014, by chenyuxin.
     */
    public static boolean isDate(String str) {
        return isDate(str, "yyyy-MM-dd");
    }
    
    /**
     * 验证日期时间
     * 
     * @param str str
     * @param pattern 验证的格式  可验证一下几种格式："yyyy-MM-dd"、"yyyy-MM-dd HH:mm:ss"、"yyyyMMdd"、"dd/MM/yyyy"
     * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isDate(String str, String pattern) {
        
        // yyyy-MM-dd
        String regex_yyyy_MM_dd = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
        // yyyy-MM-dd HH:mm:ss
        String regex_yyyy_MM_dd_HH_mm_ss = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
        // yyyyMMdd
        String regex_yyyyMMdd = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
        // dd/MM/yyyy
        String regex_dd_MM_yyyy = "(((0[1-9]|[12][0-9]|3[01])/((0[13578]|1[02]))|((0[1-9]|[12][0-9]|30)/(0[469]|11))|(0[1-9]|[1][0-9]|2[0-8])/(02))/([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}))|(29/02/(([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00)))";
        
        String regex;
        if("yyyy-MM-dd".equals(pattern)){
            regex = regex_yyyy_MM_dd;
        }else if("yyyy-MM-dd HH:mm:ss".equals(pattern)){
            regex = regex_yyyy_MM_dd_HH_mm_ss;
        }else if("yyyyMMdd".equals(pattern)){
            regex = regex_yyyyMMdd;
        }else if("dd/MM/yyyy".equals(pattern)){
            regex = regex_dd_MM_yyyy;
        }else{
            regex = regex_yyyy_MM_dd;
        }
        return match(regex, str);
    }
    
    /**
     * 验证数字输入
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsNumber(String str) {
    
        String regex = "^[0-9]*$";
        return match(regex, str);
    }
    
    /**
     * 验证非零的正整数
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIntNumber(String str) {
    
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }
    
    /**
     * 验证大写字母
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUpChar(String str) {
    
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }
    
    /**
     * 验证小写字母
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLowChar(String str) {
    
        String regex = "^[a-z]+$";
        return match(regex, str);
    }
    
    /**
     * 验证验证输入字母
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLetter(String str) {
    
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }
    
    /**
     * 验证验证输入汉字
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsChinese(String str) {
    
        String regex = "^[\u4e00-\u9fa5],{0,}$";
        return match(regex, str);
    }
    
    /**
     * 验证验证输入字符串
     * 
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsLength(String str) {
    
        String regex = "^.{8,}$";
        return match(regex, str);
    }
    
    /**
     * @param regex
     *        正则表达式字符串
     * @param str
     *        要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
    
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    /**
     * 验证日期是否符合yyyyMMdd 的日期
     * @param time
     * @return
     */
	public static boolean isFromartDate(String time) {
		if (time != null) {
			String tmp = null;
			try {
				Format f = new SimpleDateFormat("yyyyMMdd");
				Date d = (Date) f.parseObject(time);
				tmp = f.format(d);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			return tmp.equals(time);
		}else{
			return false ;
		}
	}
	
	/**
	 * 校验domain是否有效。
	 * <br>
	 * 域名中不能包含空格、逗号
	 * 2014年12月30日
	 * @param domain
	 * @return 校验有效则返回true，反之返回false
	 */
	public static boolean validateDomain(String domain) {
		if(domain.contains(" ") || domain.contains(",")) {
			return false;
		}
		return true;
	}
	
    /**
     * 去除所有空白字符
     * <b>Method</b>: RegExpValidatorUtil#replaceBlank <br/>
     * <b>Create Date</b> : 2014年11月24日
     * @param str
     * @return  String
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }    
}
