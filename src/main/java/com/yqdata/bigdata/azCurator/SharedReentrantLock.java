package com.yqdata.bigdata.azCurator;

import com.yqdata.bigdata.FileHandle;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class SharedReentrantLock {

    SharedReentrantLock sharedReentrantLock;

    InterProcessMutex interProcessMutex;

    static CuratorFramework client=null;
    static final String quorum="yqdata000:2181";

    private String path="/lock";

    public SharedReentrantLock(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client= CuratorFrameworkFactory.builder().namespace("curator").
                connectString(quorum).retryPolicy(retryPolicy).
                build();
        client.start();
        interProcessMutex=new InterProcessMutex(client,path);
    }

    public SharedReentrantLock getInstance(){
        if(sharedReentrantLock==null){
            sharedReentrantLock=new SharedReentrantLock();
        }
        return sharedReentrantLock;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean acquire() throws Exception {
        return interProcessMutex.acquire(10000, TimeUnit.SECONDS);
    }

    public void release() throws Exception {
        interProcessMutex.release();
    }

    public  void tearDown(){
        if(client!=null){
            client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        SharedReentrantLock sharedReentrantLock=new SharedReentrantLock();
        int i=0;
        sharedReentrantLock.acquire();
        boolean flag=true;

        Thread thread =new Thread(new Work());
        thread.start();

        Thread thread1 =new Thread(new Work());
        thread1.start();

        Thread thread2 =new Thread(new Work());
        thread2.start();

        while (flag){
            if (i==100){
                System.out.println("dosomething");
                sharedReentrantLock.release();
                flag=false;
            }
            System.out.println("wait");
            i++;
        }

    }
}
