package com.yqdata.bigdata.chain;

import java.util.Random;

public class KeyHandle {

    public final static String flag="_";

    public static String addSalt(String key){
        Random random = new Random();

        int num= random.nextInt(10);
       return  key+flag+num;
    }

    public static String demystify(String key){
        return  key.substring(0,key.lastIndexOf("_"));
    }

    public static void main(String[] args) {
        System.out.println(demystify(KeyHandle.addSalt("aa")));
    }
}
