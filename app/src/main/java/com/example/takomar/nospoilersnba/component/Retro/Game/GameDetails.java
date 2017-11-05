
package com.example.takomar.nospoilersnba.component.Retro.Game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameDetails {

    @SerializedName("g")
    @Expose
    private G g;

    public G getG() {
        return g;
    }

    public void setG(G g) {
        this.g = g;
    }

}
