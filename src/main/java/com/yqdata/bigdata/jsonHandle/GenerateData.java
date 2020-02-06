package com.yqdata.bigdata.jsonHandle;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class GenerateData {
    static String[] phones=new String[]{"18698956893","18698958578","18698952258","18333452258","18333452547","13785742547","137857425854"};
    static String[] ips=new String[]{"120.197.40.4","120.196.100.82","120.196.100.99","10.18.32.99"};

    static Random random7;
    static Random random4;
    static Random random100M;
    static Random random1w;

    public static int getRandom7() {
        if(random7==null){
            random7=new Random();
        }
        return random7.nextInt(7);
    }

    public static int getRandom4() {
        if(random4==null){
            random4=new Random();
        }
        return random4.nextInt(4);
    }

    public static int getRandom100M() {
        if(random100M==null){
            random100M=new Random();
        }
        return random100M.nextInt(1000000);
    }

    public static int getRandom1w() {
        if(random1w==null){
            random1w=new Random();
        }
        return random1w.nextInt(10000);
    }

    public  String javaBeanToJson(Traffic traffic){
        return JSONObject.toJSONString(traffic);
    }

    public void generate() throws IOException {
        FileUtils fileUtils=FileUtils.getWriteInstance("inputDir/traffic.txt",false);
        for (int i=0;i<=10000;i++){
            String phone=phones[getRandom7()];
            String ip=ips[getRandom4()];
            int up=getRandom1w();
            int down=getRandom100M();
            int total=up+down;
            fileUtils.writeFile(this.javaBeanToJson(new Traffic(phone,ip,up,down,total)));
        }
        fileUtils.writeInstanceClean();
    }

    public static void main(String[] args) {

        GenerateData generateData=new GenerateData();
        try {
            generateData.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
