package com.yqdata.bigdata.sparkstreaming.PreWarningTest;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.Exp;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.*;

public class PreWarningTest1 {

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("PreWarningTest1");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(5 * 1000));

        Map<String, Object> kafkaParams = new HashMap<String,Object>();
        kafkaParams.put("bootstrap.servers", "yqdata000:9093,yqdata000:9094,yqdata000:9095");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "PREWARNING_consumer1");
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("PREWARNING");

        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );


        JavaDStream<CDHRoleLog> ds = stream.map(new Function<ConsumerRecord<String, String>, CDHRoleLog>() {

            public CDHRoleLog call(ConsumerRecord<String, String> v1) throws Exception {
                String content=v1.value();
                try{
                    CDHRoleLog cDHRoleLog=null;
                    JSONObject flumeObj= JSONObject.parseObject(content);
                    return new CDHRoleLog("","",flumeObj.getString("time"),
                    flumeObj.getString("logtype"),flumeObj.getString("loginfo"));
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        });

        JavaDStream<CDHRoleLog> dsfilter=ds.filter(new Function<CDHRoleLog, Boolean>() {
            @Override
            public Boolean call(CDHRoleLog v1) throws Exception {
                if (v1==null){
                    return false;
                }
                return true;
            }
        });

        JavaDStream<CDHRoleLog>  windowDs=dsfilter.window(new Duration(5 * 1000),new Duration(5 * 1000));

        windowDs.foreachRDD(new VoidFunction<JavaRDD<CDHRoleLog>>() {
            @Override
            public void call(JavaRDD<CDHRoleLog> cdhRoleLogJavaRDD) throws Exception {
               SparkSession spark= SparkSession.builder().config(cdhRoleLogJavaRDD.context().getConf()).getOrCreate();

               Dataset<Row> ds= spark.createDataFrame(cdhRoleLogJavaRDD,CDHRoleLog.class);
               ds.show(100);
               ds.printSchema();

            }
        });

        jssc.start();
        jssc.awaitTermination();
        jssc.close();

    }


}
