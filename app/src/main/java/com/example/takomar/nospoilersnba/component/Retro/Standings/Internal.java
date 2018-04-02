
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Internal {

    @SerializedName("pubDateTime")
    @Expose
    public String pubDateTime;
    @SerializedName("xslt")
    @Expose
    public String xslt;
    @SerializedName("eventName")
    @Expose
    public String eventName;

}
