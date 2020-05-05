package com.yqdata.bigdata.cdnAccessCompres;

import com.yqdata.bigdata.FileHandle;
import com.yqdata.bigdata.cdnAccess.LogParse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;

public class AccessLogDriverComp extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if(args==null||args.length<2){
            System.out.printf("请输入输出与输入路径！！！！！");
            return 0;
        }
        int r=0;
        boolean result=true;

        try {
            Configuration configuration=super.getConf();
            configuration.set("dfs.client.use.datanode.hostname","true");
            configuration.set("dfs.replication","1");
            configuration.set("mapreduce.input.fileinputformat.split.maxsize","20971520");
            //configuration.set("dfs.block.size","134217728");

            Job job=job = Job.getInstance(configuration);

            job.setJarByClass(AccessLogDriverComp.class);
            job.setMapperClass(JsonMapper.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(NullWritable.class);
            job.setNumReduceTasks(0);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            String intput=args[0];
            //String output="hdfs://yqdata000:8020/AccessLog/";
            String output=args[1];
            //FileInputFormat.setMaxInputSplitSize(job,134217728);
            //FileInputFormat.setMinInputSplitSize(job,134217728);
            job.addCacheFile(new URI("/hive/cache/user_domain_relation.txt"));
           // job.addCacheFile(new URI("inputDir/user_domain_relation.txt"));

            FileInputFormat.setInputPaths(job,new Path(intput));
            FileOutputFormat.setOutputPath(job,new Path(output));

            result=job.waitForCompletion(true);

            Counter c=job.getCounters().findCounter("etl","totalCount");
            System.out.println(c.getName()+c.getValue());
            Counter errorC=job.getCounters().findCounter("etl","ErrorCount");
            System.out.println(errorC.getName()+errorC.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result?0:1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration=new Configuration();
        //args=new String[]{"inputDir/accesslog.txt","output"};
        ToolRunner.run(configuration,new AccessLogDriverComp(),args);
    }
}

class JsonMapper extends Mapper<LongWritable, Text,Text ,NullWritable> {
    private static Logger log4j=Logger.getLogger(JsonMapper.class);

    HashMap<String,String> userDomain=new HashMap<String,String>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI uri =context.getCacheFiles()[0];
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
            String[] data=line.split("\t");
            userDomain.put(data[0],data[1]);
        }
        bufferedReader.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if(value==null)
            return;
        context.getCounter("etl","totalCount").increment(1);

        try {
            context.write(new Text(LogParse.parse(value.toString(),userDomain).toString()),NullWritable.get());
        } catch (ParseException e) {
            log4j.error("error text    "+value.toString());
            context.getCounter("etl","ErrorCount").increment(1);
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            log4j.error("error text    "+value.toString());
            context.getCounter("etl","ErrorCount").increment(1);
            e.printStackTrace();
        }
    }



}
