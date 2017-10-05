
package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class H {

    @SerializedName("tid")
    @Expose
    private Integer tid;
    @SerializedName("re")
    @Expose
    private String re;
    @SerializedName("ta")
    @Expose
    private String ta;
    @SerializedName("tn")
    @Expose
    private String tn;
    @SerializedName("tc")
    @Expose
    private String tc;
    @SerializedName("s")
    @Expose
    private String s;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public String getTa() {
        return ta;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tid", tid).append("re", re).append("ta", ta).append("tn", tn).append("tc", tc).append("s", s).toString();
    }

}
