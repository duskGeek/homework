package com.yqdata.bigdata.chain;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Random;

public class WcDriverBreakUp {

    public static void run(Configuration configuration){

        try {
            Job job=job = Job.getInstance(configuration);

            job.setJarByClass(WcDriverBreakUp.class);
            job.setMapperClass(WcMapperBup.class);
            job.setReducerClass(WcReduceBup.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            String intput="inputDir/car.txt";
            String output="output";

            FileInputFormat.setInputPaths(job,new Path(intput));
            FileOutputFormat.setOutputPath(job,new Path(output));

            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}


class WcMapperBup extends Mapper<LongWritable, Text,Text,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if(value==null)
            return;

        String[] array= value.toString().split(",");
        for (String k:array ) {
            context.write(new Text(KeyHandle.addSalt(k)),new IntWritable(1));
        }
    }

}


class WcReduceBup extends Reducer<Text, IntWritable,Text,IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int valSum=0;
        for (IntWritable val:values) {
            valSum+=val.get();
        }
        context.write(key,new IntWritable(valSum));

    }
}