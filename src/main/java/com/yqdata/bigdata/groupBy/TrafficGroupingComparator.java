package com.yqdata.bigdata.groupBy;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class TrafficGroupingComparator extends WritableComparator {

    public TrafficGroupingComparator(){
        super(Traffic.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Traffic beforeTraffic=(Traffic) a;
        Traffic afterTraffic=(Traffic) b;
        if(beforeTraffic.getIp().equals(afterTraffic.getIp())&&
                beforeTraffic.getPhone().equals(afterTraffic.getPhone())){
            return 1;
        }else{
            return 0;
        }
    }
}
