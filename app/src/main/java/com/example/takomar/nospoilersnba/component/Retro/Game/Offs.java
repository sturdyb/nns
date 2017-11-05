
package com.example.takomar.nospoilersnba.component.Retro.Game;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offs {

    @SerializedName("off")
    @Expose
    private List<Off> off = null;

    public List<Off> getOff() {
        return off;
    }

    public void setOff(List<Off> off) {
        this.off = off;
    }

}
