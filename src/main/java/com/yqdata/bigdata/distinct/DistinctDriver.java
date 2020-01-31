package com.yqdata.bigdata.distinct;

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

public class DistinctDriver {

    public static void main(String[] args) {
        try {

        Configuration configuration=new Configuration();
        Job job=job = Job.getInstance(configuration);

        job.setJarByClass(DistinctDriver.class);
        job.setMapperClass(DistinctMapper.class);
        job.setReducerClass(DistinctReduce.class);

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
}


class DistinctMapper extends Mapper<LongWritable, Text,Text,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if(value==null)
            return;

        String[] array= value.toString().split(",");

        for (String k:array ) {
            context.write(new Text(k),new IntWritable(1));
        }
    }
}


class DistinctReduce extends Reducer<Text, IntWritable,Text, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        context.write(key,NullWritable.get());

    }
}