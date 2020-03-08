package com.yqdata.bigdata;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.BeforeClass;
import org.junit.Test;

public class azCuratorTest {

    static CuratorFramework client=null;
    static final String quorum="yqdata000:2181";

    @Test
    public void createNode() throws Exception {
        client.create().creatingParentContainersIfNeeded().
                withMode(CreateMode.PERSISTENT).
                withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).
                forPath("/lock","78".getBytes());
    }

    @Test
    public void deletNode() throws Exception {
        client.delete().inBackground().forPath("/first/createnode");
    }

    @Test
    public void setNode() throws Exception {
        client.setData().inBackground().forPath("/first/createnode","99".getBytes());
    }

    @Test
    public void watchNode() throws Exception {
        PathChildrenCache cache=new PathChildrenCache(client, "/first", true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType())
                {
                    case CHILD_UPDATED:
                        System.out.println("Node CHILD_UPDATED: " +
                                ZKPaths.getNodeFromPath(pathChildrenCacheEvent.getData().getPath())+
                                " value:"+new String((byte[])pathChildrenCacheEvent.getData().getData()));
                        break;
                }
            }
        });
        Thread.sleep(1000000);
    }

    @BeforeClass
    public static void  setUp(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client=CuratorFrameworkFactory.builder().namespace("curator").
                connectString(quorum).retryPolicy(retryPolicy).
                build();
        client.start();
    }

    public void tearDown(){

    }
}
