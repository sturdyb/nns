
package com.example.takomar.nospoilersnba.component.Retro.Game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lpla {

    @SerializedName("evt")
    @Expose
    private Integer evt;
    @SerializedName("cl")
    @Expose
    private String cl;
    @SerializedName("de")
    @Expose
    private String de;
    @SerializedName("locX")
    @Expose
    private Integer locX;
    @SerializedName("locY")
    @Expose
    private Integer locY;
    @SerializedName("opt1")
    @Expose
    private Integer opt1;
    @SerializedName("opt2")
    @Expose
    private Integer opt2;
    @SerializedName("mtype")
    @Expose
    private Integer mtype;
    @SerializedName("etype")
    @Expose
    private Integer etype;
    @SerializedName("opid")
    @Expose
    private String opid;
    @SerializedName("tid")
    @Expose
    private Integer tid;
    @SerializedName("pid")
    @Expose
    private Integer pid;
    @SerializedName("hs")
    @Expose
    private Integer hs;
    @SerializedName("vs")
    @Expose
    private Integer vs;
    @SerializedName("epid")
    @Expose
    private String epid;
    @SerializedName("oftid")
    @Expose
    private Integer oftid;

    public Integer getEvt() {
        return evt;
    }

    public void setEvt(Integer evt) {
        this.evt = evt;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public Integer getLocX() {
        return locX;
    }

    public void setLocX(Integer locX) {
        this.locX = locX;
    }

    public Integer getLocY() {
        return locY;
    }

    public void setLocY(Integer locY) {
        this.locY = locY;
    }

    public Integer getOpt1() {
        return opt1;
    }

    public void setOpt1(Integer opt1) {
        this.opt1 = opt1;
    }

    public Integer getOpt2() {
        return opt2;
    }

    public void setOpt2(Integer opt2) {
        this.opt2 = opt2;
    }

    public Integer getMtype() {
        return mtype;
    }

    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    public Integer getEtype() {
        return etype;
    }

    public void setEtype(Integer etype) {
        this.etype = etype;
    }

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getHs() {
        return hs;
    }

    public void setHs(Integer hs) {
        this.hs = hs;
    }

    public Integer getVs() {
        return vs;
    }

    public void setVs(Integer vs) {
        this.vs = vs;
    }

    public String getEpid() {
        return epid;
    }

    public void setEpid(String epid) {
        this.epid = epid;
    }

    public Integer getOftid() {
        return oftid;
    }

    public void setOftid(Integer oftid) {
        this.oftid = oftid;
    }

}
