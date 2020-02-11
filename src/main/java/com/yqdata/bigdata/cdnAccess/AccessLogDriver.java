package com.yqdata.bigdata.cdnAccess;

import com.alibaba.fastjson.JSONObject;
import com.yqdata.bigdata.jsonHandle.Traffic;
import com.yqdata.utils.GetIPAddress;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class AccessLogDriver {

    public static void main(String[] args) {
        try {

        Configuration configuration=new Configuration();
        Job job=job = Job.getInstance(configuration);

        job.setJarByClass(AccessLogDriver.class);
        job.setMapperClass(JsonMapper.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        String intput="inputDir/accesslog.txt";
        String output="output";

        FileInputFormat.setInputPaths(job,new Path(intput));
        FileOutputFormat.setOutputPath(job,new Path(output));

        job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class JsonMapper extends Mapper<LongWritable, Text,Text ,NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if(value==null)
            return;

        String[] log= value.toString().split("\t");

        String ip=log[1];

        String region= GetIPAddress.search(ip);
        String country="";
        String prov="";
        String city="";
        if(region!=null){
            String[] adress= region.split("\\|");
            if (adress!=null&&adress.length>0){
                country=adress[0];
                prov=adress[2];
                city=adress[3];
            }
        }
        context.write(new Text(value.toString()+
                "\t"+country+"\t"+prov+"\t"+city),NullWritable.get());
    }
}
