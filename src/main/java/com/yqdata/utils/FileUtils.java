package com.yqdata.utils;

import java.io.*;

/**
 * 文件工具类
 */
public class FileUtils {

    private static FileUtils fileUtils;
    private static BufferedWriter bw;

    private FileUtils(){}

    private FileUtils(String readPath,String writePath) throws IOException {
        this( readPath, writePath,false);
    }

    private FileUtils(String readPath,String writePath,boolean isAppend) throws IOException {
        if(readPath!=null){

        }
        if(writePath!=null){
            File file=new File(writePath);
            if(!file.exists()){
                file.createNewFile();
            }
            bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,isAppend)));
        }
    }

    public static FileUtils getWriteInstance(String path,boolean isAppend) throws IOException {
        if(fileUtils==null){
            fileUtils=new FileUtils(null,path,isAppend);
        }
        return fileUtils;
    }

    public  void writeFile(String context) throws IOException {
        this.writeFile( context,true);
    }
    public  void writeFile(String context,boolean nextNewline) throws IOException {
        bw.write(context);
        if(nextNewline){
            bw.newLine();
        }
    }

    public void writeInstanceClean() throws IOException {
        if(bw!=null){
            bw.flush();
            bw.close();
            bw=null;
        }
    }


}
