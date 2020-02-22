package com.yqdata.bigdata.cdnAccess;

import com.yqdata.utils.GetIPAddress;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class LogParse {
    static DateFormat df=new SimpleDateFormat("[d/MMM/yyyy:HH:mm:ss Z]", Locale.UK);
    static Calendar calendar=Calendar.getInstance();

    public static AccessLogAfter parse(String text) throws ParseException {
        String[] log= text.toString().split("\t");
        String time=log[0];
        String ip=log[1];
        String proxyIp=log[2];
        String responseTime=log[3];
        String referer=log[4];
        String method=log[5];
        String url=log[6];
        String httpCode=log[7];
        String requestSize=log[8];
        String responseSize=log[9];
        String hitCache=log[10];
        String userAgent=log[11];
        String fileType=log[12];

        String region= GetIPAddress.search(ip);
        String country="";
        String prov="";
        String city="";
        String year="";
        String day="";
        String month="";
        if(time!=null){
            Date date=df.parse(time);
            calendar.setTime(date);

            int m=calendar.get(Calendar.MONTH);
            int d=calendar.get(Calendar.DAY_OF_MONTH);
            year=String.valueOf(calendar.get(Calendar.YEAR));
            month=String.valueOf(m<10?"0"+m:m);
            day=String.valueOf(d<10?"0"+d:d);
        }

        if(region!=null){
            String[] adress= region.split("\\|");
            if (adress!=null&&adress.length>0){
                country=adress[0];
                prov=adress[2];
                city=adress[3];
            }
        }

        String[] urlArray=URLParsing(url);
        String protocol=urlArray[0];
        String domain=urlArray[1];
        String path=urlArray[2];
        String query=urlArray[3];

        return new AccessLogAfter(ip,  proxyIp,  Integer.parseInt(responseTime),  referer,  method,httpCode,
                requestSize,  responseSize,  hitCache,  userAgent,
                fileType,  country,  prov,  city,  year,  day,
                month,  protocol,  domain,  path,  query);
    }


    public static String[] URLParsing(String url){
        String domain="";
        String path="";
        String protocol="";
        String query="";

        if(url!=null){
            if(url.indexOf("?")>0)
            {
                query=url.substring(url.indexOf("?")).substring(1);
            }
            if(url.indexOf("://")>0)
            {
                protocol=url.substring(0,url.indexOf("://"))+"://";

                String domainPath=url.substring(url.indexOf(protocol)+protocol.length()
                        ,url.indexOf("?")>0?url.indexOf("?"):url.length()-1);

                domain=domainPath.substring(0,domainPath.indexOf("/"));
                path=domainPath.substring(domainPath.indexOf("/"));
            }
        }

        return new String[]{protocol,domain,path,query};
    }

    public static void main(String[] args) throws ParseException {
        System.out.printf(parse("[11/Feb/2020:00:00:01 +0800]\t123.234.24.250\t-\t6268\t-\tGET\thttp://www.aliyun." +
                "com/index.html?id=63589\t200\t591\t15672\tMISS\tMozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36\ttext/html\t\n").toString());
    }

}
