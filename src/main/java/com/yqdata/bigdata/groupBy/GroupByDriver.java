package com.yqdata.bigdata.groupBy;

import org.apache.commons.collections.IteratorUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupByDriver {

    public static void main(String[] args) {
        try {

        Configuration configuration=new Configuration();
        Job job=job = Job.getInstance(configuration);

        job.setJarByClass(GroupByDriver.class);
        job.setMapperClass(GroupByMapper.class);
        job.setReducerClass(GroupByReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Traffic.class);

       // job.setGroupingComparatorClass(TrafficGroupingComparator.class);

        job.setOutputKeyClass(Traffic.class);
        job.setOutputValueClass(NullWritable.class);

        String intput="inputDir/access2.log";
        String output="output";

        FileInputFormat.setInputPaths(job,new Path(intput));
        FileOutputFormat.setOutputPath(job,new Path(output));

        job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class GroupByMapper extends Mapper<LongWritable, Text,Text,Traffic> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if(value==null)
            return;

        String[] array= value.toString().split("\t");

        Traffic traffic = new Traffic(array[1], array[3],
                Integer.parseInt(array[8]),
                Integer.parseInt(array[9])
                , Integer.parseInt(array[8]) + Integer.parseInt(array[9]));

        context.write(new Text(traffic.getPhone()+traffic.getIp()) ,traffic);
    }
}
class GroupByReduce extends Reducer<Text, Traffic,Traffic, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<Traffic> values, Context context) throws IOException, InterruptedException {
        String phone="";
        String ip="";
        int up=0;
        int down=0;
        int total=0;

        for (Traffic val:values) {
            phone=val.getPhone();
            ip=val.getIp();
            up+=val.getUp();
            down+=val.getDown();
            total+=val.getTotal();
        }

        Traffic traffic = new Traffic(phone,ip,up,down,total);
        context.write(traffic,NullWritable.get());

    }
}