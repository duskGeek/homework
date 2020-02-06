package com.yqdata.bigdata.jsonHandle;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Traffic implements WritableComparable<Traffic> {

    private String phone;
    private String ip;

    private int up;
    private int down;
    private int total;

    public Traffic(){

    }

    public Traffic(String phone, String ip, int up, int down, int total){
        this.phone=phone;
        this.ip=ip;
        this.up=up;
        this.down=down;
        this.total=total;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.phone);
        out.writeUTF(this.ip);
        out.writeInt(this.up);
        out.writeInt(this.down);
        out.writeInt(this.total);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.phone=in.readUTF();
        this.ip=in.readUTF();
        this.up=in.readInt();
        this.down=in.readInt();
        this.total=in.readInt();
    }

    @Override
    public String toString() {
        return phone+"\t"+ip+"\t"+up+"\t"+down+"\t"+total;
    }


    @Override
    public int compareTo(Traffic o) {

        return o.getTotal()>this.getTotal()?1:-1;
    }
}
