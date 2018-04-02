
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standard {

    @SerializedName("seasonYear")
    @Expose
    public Integer seasonYear;
    @SerializedName("seasonStageId")
    @Expose
    public Integer seasonStageId;
    @SerializedName("conference")
    @Expose
    public Conference conference;

}
