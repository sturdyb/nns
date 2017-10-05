
package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import com.example.takomar.nospoilersnba.component.Retro.V;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class G {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("gcode")
    @Expose
    private String gcode;
    @SerializedName("seri")
    @Expose
    private String seri;
    @SerializedName("is")
    @Expose
    private Integer is;
    @SerializedName("gdte")
    @Expose
    private String gdte;
    @SerializedName("htm")
    @Expose
    private String htm;
    @SerializedName("vtm")
    @Expose
    private String vtm;
    @SerializedName("etm")
    @Expose
    private String etm;
    @SerializedName("an")
    @Expose
    private String an;
    @SerializedName("ac")
    @Expose
    private String ac;
    @SerializedName("as")
    @Expose
    private String as;
    @SerializedName("st")
    @Expose
    private String st;
    @SerializedName("stt")
    @Expose
    private String stt;
    @SerializedName("bd")
    @Expose
    private Bd bd;
    @SerializedName("v")
    @Expose
    private V v;
    @SerializedName("h")
    @Expose
    private H h;
    @SerializedName("gweek")
    @Expose
    private Object gweek;
    @SerializedName("gdtutc")
    @Expose
    private String gdtutc;
    @SerializedName("utctm")
    @Expose
    private String utctm;
    @SerializedName("ppdst")
    @Expose
    private String ppdst;
    @SerializedName("seq")
    @Expose
    private Integer seq;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGcode() {
        return gcode;
    }

    public void setGcode(String gcode) {
        this.gcode = gcode;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public Integer getIs() {
        return is;
    }

    public void setIs(Integer is) {
        this.is = is;
    }

    public String getGdte() {
        return gdte;
    }

    public void setGdte(String gdte) {
        this.gdte = gdte;
    }

    public String getHtm() {
        return htm;
    }

    public void setHtm(String htm) {
        this.htm = htm;
    }

    public String getVtm() {
        return vtm;
    }

    public void setVtm(String vtm) {
        this.vtm = vtm;
    }

    public String getEtm() {
        return etm;
    }

    public void setEtm(String etm) {
        this.etm = etm;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public Bd getBd() {
        return bd;
    }

    public void setBd(Bd bd) {
        this.bd = bd;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public H getH() {
        return h;
    }

    public void setH(H h) {
        this.h = h;
    }

    public Object getGweek() {
        return gweek;
    }

    public void setGweek(Object gweek) {
        this.gweek = gweek;
    }

    public String getGdtutc() {
        return gdtutc;
    }

    public void setGdtutc(String gdtutc) {
        this.gdtutc = gdtutc;
    }

    public String getUtctm() {
        return utctm;
    }

    public void setUtctm(String utctm) {
        this.utctm = utctm;
    }

    public String getPpdst() {
        return ppdst;
    }

    public void setPpdst(String ppdst) {
        this.ppdst = ppdst;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("gid", gid).append("gcode", gcode).append("seri", seri).append("is", is).append("gdte", gdte).append("htm", htm).append("vtm", vtm).append("etm", etm).append("an", an).append("ac", ac).append("as", as).append("st", st).append("stt", stt).append("bd", bd).append("v", v).append("h", h).append("gweek", gweek).append("gdtutc", gdtutc).append("utctm", utctm).append("ppdst", ppdst).append("seq", seq).toString();
    }

}
