package com.yqdata.bigdata.cdnAccess;

import com.alibaba.fastjson.JSONObject;
import com.yqdata.bigdata.jsonHandle.Traffic;
import com.yqdata.utils.GetIPAddress;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileAsBinaryOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

public class AccessLogDriver {

    public static void main(String[] args) {
        try {

        Configuration configuration=new Configuration();
        configuration.set("dfs.client.use.datanode.hostname","true");
        configuration.set("dfs.replication","1");
        configuration.set("dfs.block.size","134217728");

        Job job=job = Job.getInstance(configuration);

        job.setJarByClass(AccessLogDriver.class);
        job.setMapperClass(JsonMapper.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        String intput="inputDir/accesslog.txt";
        String output="hdfs://yqdata000:8020/json";
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.setMaxInputSplitSize(job,134217728);
        FileInputFormat.setMinInputSplitSize(job,134217728);
        FileInputFormat.setInputPaths(job,new Path(intput));
        FileOutputFormat.setOutputPath(job,new Path(output));


        job.waitForCompletion(true);

            Counter c=job.getCounters().findCounter("etl","totalCount");
            System.out.println(c.getName()+c.getValue());
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
        context.getCounter("etl","totalCount").increment(1);
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
