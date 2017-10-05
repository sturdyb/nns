package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by takomar on 9/24/17.
 */


public class NbaGames {

    @SerializedName("lscd")
    @Expose
    private List<Lscd> lscd = null;

    public List<Lscd> getLscd() {
        return lscd;
    }

    public void setLscd(List<Lscd> lscd) {
        this.lscd = lscd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lscd", lscd).toString();
    }

}
