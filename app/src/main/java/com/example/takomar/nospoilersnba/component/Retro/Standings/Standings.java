
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standings {

    @SerializedName("_internal")
    @Expose
    public Internal internal;
    @SerializedName("league")
    @Expose
    public League league;

}
