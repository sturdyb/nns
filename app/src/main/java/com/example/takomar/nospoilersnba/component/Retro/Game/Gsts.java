
package com.example.takomar.nospoilersnba.component.Retro.Game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gsts {

    @SerializedName("lc")
    @Expose
    private Integer lc;
    @SerializedName("tt")
    @Expose
    private Integer tt;

    public Integer getLc() {
        return lc;
    }

    public void setLc(Integer lc) {
        this.lc = lc;
    }

    public Integer getTt() {
        return tt;
    }

    public void setTt(Integer tt) {
        this.tt = tt;
    }

}
