package com.example.myapplication.model;


public class Calendar {
    private int cid;
    private String cname;
    private String ename;

    public Calendar() {}

    public Calendar(int cid, String cname, String ename) {
        this.cid = cid;
        this.cname = cname;
        this.ename = ename;
    }




    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }


    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "cid=" + cid +
                ", cname='" + cname + '\'' +
                ", ename='" + ename + '\'' +
                '}';
    }
}

