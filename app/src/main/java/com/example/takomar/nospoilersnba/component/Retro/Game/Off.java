
package com.example.takomar.nospoilersnba.component.Retro.Game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Off {

    @SerializedName("fn")
    @Expose
    private String fn;
    @SerializedName("ln")
    @Expose
    private String ln;
    @SerializedName("num")
    @Expose
    private String num;

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
