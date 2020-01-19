package com.yqdata.bigdata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class FileHandleTest {

    static  FileHandle fileHandle;

    @BeforeClass
    public static void setUp() throws InterruptedException, IOException, URISyntaxException {
         fileHandle = FileHandle.getInstance();
    }

    @Test
    public void execute() throws Exception {

        int r=fileHandle.execute("20120101");
        Assert.assertEquals(4,r);
    }
}
