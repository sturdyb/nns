
package com.example.takomar.nospoilersnba.component.Retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Mscd {

    @SerializedName("mon")
    @Expose
    private String mon;
    @SerializedName("g")
    @Expose
    private List<G> g = null;

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public List<G> getG() {
        return g;
    }

    public void setG(List<G> g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mon", mon).append("g", g).toString();
    }

}
