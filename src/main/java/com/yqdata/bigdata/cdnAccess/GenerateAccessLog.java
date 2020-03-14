package com.yqdata.bigdata.cdnAccess;

import com.alibaba.fastjson.JSONObject;
import com.yqdata.bigdata.jsonHandle.Traffic;
import com.yqdata.utils.FileUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GenerateAccessLog {
    static String[] phones=new String[]{"18698956893","18698958578","18698952258","18333452258","18333452547","13785742547","137857425854"};
    static String[] ips=new String[]{"120.197.40.4","120.196.100.82","120.196.100.99","10.18.32.99"};
    static String[] domains=new String[]{"www.taobao.com","www.aliyun.com","www.baidu.com","www.qq.com"};

    //27.16.0.0	27.16.124.255
    private static int intervalNum=11;
    private static int addTime=1;
    private static int addTimeUtil=Calendar.SECOND;

    static Random random;

    //[9/Jun/2015:01:58:09 +0800]
    static DateFormat df=new SimpleDateFormat("[d/MMM/yyyy:HH:mm:ss Z]", Locale.UK);

    static Calendar cal=Calendar.getInstance();
    static {
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
    }

    public static int getRandom(int bound) {
        if(random==null){
            random=new Random();
        }
        return random.nextInt(bound);
    }

    public  String javaBeanToJson(AccessLog accessLog){
        return JSONObject.toJSONString(accessLog);
    }

    public void generate() throws IOException {


        FileUtils fileUtils=FileUtils.getWriteInstance("inputDir/accesslog.txt",false);

        for (int i=0;i<=1000000;i++){
            fileUtils.writeFile(mockObject(i).toString());
        }
        fileUtils.writeInstanceClean();
    }


    public static AccessLog mockObject(int i ){
        String time =getTime(i,cal);
        String accessIp=GenerateIP.getRandomIp();
        String proxyIp="-";
        int responseTime=getRandom(10000);
        String referer="-";
        String method="GET";
        String url="http://"+domains[getRandom(4)]+"/index.html?id="+getRandom(100000);
        String httpCode="200";
        String requestSize=getRandom(2000)+"";
        String responseSize=getRandom(20000)+"";
        String hitCache="MISS";
        String userAgent=GenerateUserAgent.getUserAgent(getRandom(131));
        String fileType="text/html";
        return new AccessLog( time,  accessIp,  proxyIp,  responseTime,  referer,  method,
                url,  httpCode,  requestSize,  responseSize,  hitCache,
                userAgent,  fileType);
    }

    public static String getTime(int position, Calendar cal){
        if(position % intervalNum==0){
            cal.add(addTimeUtil,addTime);
        }
        Date date=cal.getTime();
        return df.format(date);
    }

    public static void main(String[] args) throws IOException {
        GenerateAccessLog generateData=new GenerateAccessLog();
        generateData.generate();

    }

}
