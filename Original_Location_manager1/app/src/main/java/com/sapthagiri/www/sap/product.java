package com.sapthagiri.www.sap;

public class product {
    private int uid;
    private String uname;
    private String pass;
    private int rid;
    private String ema;
    private int mnum;
    private String sta;
    private String dob;
    private String gen;
    private String bran;

    public product( String uname, String ema, int  mnum, String dob,String gen,String bran) {
        this.uid = uid;
        this.uname = uname;
        this.pass = pass;
        this.rid = rid;
        this.ema = ema;
        this.mnum = mnum;
        this.sta = sta;
        this.dob = dob;
        this.gen = gen;
        this.bran=bran;

    }

    public int getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getPass() {
        return pass;
    }

    public int getRid() {
        return rid;
    }

    public String getEma() {
        return ema;
    }

    public int getMnum() {
        return mnum;
    }
    public String getSta() {
        return sta;
    }
    public String getDob() {
        return dob;
    }
    public String getGen() {
        return gen;
    }
    public String getBran()
    {
        return bran;
    }


}