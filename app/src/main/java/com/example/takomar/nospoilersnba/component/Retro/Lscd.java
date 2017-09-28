package com.example.takomar.nospoilersnba.component.Retro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by takomar on 9/24/17.
 */

public class Lscd {

    @SerializedName("mscd")
    @Expose
    private Mscd mscd;

    public Mscd getMscd() {
        return mscd;
    }

    public void setMscd(Mscd mscd) {
        this.mscd = mscd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mscd", mscd).toString();
    }

}