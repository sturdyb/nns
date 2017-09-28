
package com.example.takomar.nospoilersnba.component.Retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class B {

    @SerializedName("seq")
    @Expose
    private Integer seq;
    @SerializedName("disp")
    @Expose
    private String disp;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("lan")
    @Expose
    private String lan;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("seq", seq).append("disp", disp).append("scope", scope).append("type", type).append("lan", lan).toString();
    }

}
