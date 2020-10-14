package com.njupt.crm.settings;

import com.njupt.crm.utils.DateTimeUtil;
import com.njupt.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
//        失效时间
        String expirTime = "2020-11-11 10:10:10";
//        当前时间
        String currentTime = DateTimeUtil.getSysTime();
//        判断是否失效
        int count = expirTime.compareTo(currentTime);
//        大于0未失效，小于0失效
        System.out.println(count);

//        判断锁定
        String lockStatement = "0";
        if("0".equals(lockStatement)){
            System.out.println("锁定！");
        }else {
            System.out.println("未锁定！");
        }

        String ip = "192.168.1.1";
        String allowIP = "192.168.1.1,192.168.1.2";
        if(allowIP.contains(ip)){
            System.out.println("ip允许登录");
        }else {
            System.out.println("ip受限");
        }

        String password = "19043985720-9";
        String MD5Password = MD5Util.getMD5(password);
        System.out.println(MD5Password);

//        Date data = new Date();
////        System.out.println(data);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String str = sdf.format(data);
//        System.out.println(str);
    }



}
