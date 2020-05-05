package com.yqdata.bigdata.keywordCount;

import com.yqdata.bigdata.FileHandle;
import com.yqdata.bigdata.cdnAccess.AccessLogDriver;
import com.yqdata.utils.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

public class KeywordCountDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        boolean result=false;
        try {
            if(args==null||args.length<1){
                System.out.printf("请输入需要统计的关键字！！");
            }
            //String keywords="baoma,dazhong,jiebao";
            String keywords=args[0];
            String keywordsfille="/data/kw/keywords";
            Configuration configuration=new Configuration();
            Job job=job = Job.getInstance(configuration);

            job.setJarByClass(KeywordCountDriver.class);
            job.setMapperClass(keywordCountMapper.class);
            job.setReducerClass(keywordCountReduce.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

//            String intput="inputDir/car.txt";
//            String output="output";
            String intput="/data/kw/car.txt";
            String output="/data/kw/keywordoutput";
            FileInputFormat.setInputPaths(job,new Path(intput));
            FileOutputFormat.setOutputPath(job,new Path(output));


            FileHandle fh= FileHandle.getInstance();
            fh.writeFile(keywordsfille,keywords);

            job.setCacheFiles(new URI[]{new URI(keywordsfille)});
            result=job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result?0:1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration=new Configuration();
        ToolRunner.run(configuration,new KeywordCountDriver(),args);
    }
}

class keywordCountMapper extends Mapper<LongWritable, Text,Text,IntWritable> {
    Set<String> keyWord=new HashSet<String>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI uri=context.getCacheFiles()[0];
        FileHandle fileHandle;
        try {
            fileHandle=FileHandle.getInstance();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(fileHandle.getFile(uri.toString())));
        String line="";
        while(( line =bufferedReader.readLine())!=null){
            String[] data=line.split(",");
            for (String keyword:data) {
                keyWord.add(keyword);
            }
        }
        bufferedReader.close();

    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if(value==null)
            return;
        String[] array= value.toString().split(",");

        for (String k:array ) {
            if(keyWord.contains(k)){
                context.write(new Text(k),new IntWritable(1));
            }
        }
    }
}

class keywordCountReduce extends Reducer<Text, IntWritable,NullWritable, Text> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int i=0;
        for (IntWritable v:values) {
            i+=v.get();
        }
        String out=key+","+ i;
        context.write(NullWritable.get(),new Text(out));
    }
}