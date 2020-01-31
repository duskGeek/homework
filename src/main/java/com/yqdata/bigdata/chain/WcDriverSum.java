package com.yqdata.bigdata.chain;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WcDriverSum {

    public static void run(Configuration configuration) {
        try {
            Job job = job = Job.getInstance(configuration);

            job.setJarByClass(WcDriverSum.class);
            job.setMapperClass(WcMapperSum.class);
            job.setReducerClass(WcReduceSum.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            String intput = "output";
            String output = "output2";

            FileInputFormat.setInputPaths(job, new Path(intput));
            FileOutputFormat.setOutputPath(job, new Path(output));

            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {


    }
}

class WcMapperSum extends Mapper<LongWritable, Text,Text,IntWritable> {

    String isFilter="1";
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fileSplit=(FileSplit)context.getInputSplit();
        Path path=fileSplit.getPath();
        String fileName=path.getName();
        if (!fileName.contains("part-r")||fileName.contains(".crc")){
            isFilter="0";
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if (isFilter.equals("0")){
            return ;
        }

        if(value==null)
            return;

        String[] array= value.toString().split("\t");
        if(array==null||array.length<=0){
            return ;
        }
        context.write(new Text(KeyHandle.demystify(array[0])),new IntWritable(Integer.parseInt(array[1])));
    }

}


class WcReduceSum extends Reducer<Text, IntWritable,Text,IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int valSum=0;
        for (IntWritable val:values) {
            valSum+=val.get();
        }
        context.write(key,new IntWritable(valSum));

    }
}