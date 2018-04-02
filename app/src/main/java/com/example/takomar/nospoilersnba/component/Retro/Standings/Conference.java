
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conference {

    @SerializedName("east")
    @Expose
    public List<Standing> east = null;
    @SerializedName("west")
    @Expose
    public List<Standing> west = null;

}
