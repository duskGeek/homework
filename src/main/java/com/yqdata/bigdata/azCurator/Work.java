package com.yqdata.bigdata.azCurator;

import java.util.concurrent.TimeUnit;

public class Work implements Runnable {

    @Override
    public void run() {
        SharedReentrantLock sharedReentrantLock=new SharedReentrantLock();
        try {
            sharedReentrantLock.acquire();
            System.out.println("Work dosoming...."+this.toString());
            sharedReentrantLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
