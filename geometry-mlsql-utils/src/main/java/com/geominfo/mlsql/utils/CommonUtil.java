package com.geominfo.mlsql.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: geometry-bi
 * @description: 高频方法工具类
 * @author: 肖乔辉
 * @create: 2019-05-23 19:02
 * @version: 1.0.0
 */
public class CommonUtil {

    /* *
     * @Description 获取指定位数的随机数
     * @Param [length]
     * @Return java.lang.String
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 当前对象所有数据是否存在空数据
     * @param obj 对象
     * @return
     * @throws Exception
     */
    public static boolean isHaveDataNull(Object obj) {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        boolean flag = false;
        try {
            for (Field f : fs) {//遍历属性
                f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                Object val = f.get(obj);// 得到此属性的值
                if (val != null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                    flag = true;
                    break;
                }
            }
        }catch (IllegalAccessException e){

        }
        return flag;
    }

    /**
      * @description: 将字节转化为KB,MB,GB
      *
      * @author: 李豪珣
      *
      * @date: 2018/12/8
      *
      * @param:
      *
      * @return:
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal gibibyte = new BigDecimal(1024*1024*1024);
        float returnValue = filesize.divide(gibibyte, 2, BigDecimal.ROUND_UP).floatValue();
        /*divide(BigDecimal divisor, int scale, int roundingMode) 返回一个BigDecimal，其值为（this/divisor），其标度为指定标度*/
        if(returnValue > 10) {
            return (returnValue + "GB");
        } else {
            BigDecimal mebibyte = new BigDecimal(1024*1024);
            returnValue = filesize.divide(mebibyte, 2, BigDecimal.ROUND_UP).floatValue();
            if(returnValue > 1) {
                return (returnValue + "MB");
            } else {
                BigDecimal kibibyte = new BigDecimal(1024);
                returnValue = filesize.divide(kibibyte, 2,BigDecimal.ROUND_UP).floatValue();
                return (returnValue + "KB");
            }
        }
    }

    /**
     * 去除字符串中的回车、换行符、制表符
     * 注：\n 回车 \t 水平制表符 \s 空格 \r 换行
     * @param str
     * @return
     */
    public static String replaceBlank(String str,String character) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile(character);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * string转unicode
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    /**
     * unicode转string
     * @param unicode
     * @return
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }


    /**
     *
     * @param dateTimes 毫秒时间值
     * @return yyyy-MM-dd HH:mm:SS时间类型字符串
     */
    public static String dateTime(Long dateTimes){
        Date date = new Date();
        date.setTime(dateTimes);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(date);
    }

}
