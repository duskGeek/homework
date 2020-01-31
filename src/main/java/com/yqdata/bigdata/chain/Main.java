package com.yqdata.bigdata.chain;

import org.apache.hadoop.conf.Configuration;

public class Main {

    public static void main(String[] args) {
        Configuration configuration=new Configuration();

        WcDriverBreakUp.run(configuration);
        WcDriverSum.run(configuration);
    }
}
