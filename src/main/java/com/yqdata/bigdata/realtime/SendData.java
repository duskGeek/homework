package com.yqdata.bigdata.realtime;

import com.yqdata.bigdata.cdnAccess.AccessLog;
import com.yqdata.bigdata.cdnAccess.GenerateAccessLog;

import java.util.HashMap;
import java.util.Map;

public class SendData {
    public static String dataToServer(String url,String data){
        Map<String,String> reqMap=new HashMap<String,String>();
        reqMap.put("remoteLog",data);
        return HttpClientUtil.get(url,reqMap);
    }

    public static void generateLogs(String domaim) throws InterruptedException {
        for (int i=0;i<100000;i++){
            AccessLog accessLog=GenerateAccessLog.mockObject(i);
            dataToServer(domaim+"/receiveLog",accessLog.toString());
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        try {
            SendData.generateLogs("http://yqdata000:10087");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
