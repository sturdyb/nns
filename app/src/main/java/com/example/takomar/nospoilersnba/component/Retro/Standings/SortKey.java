
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortKey {

    @SerializedName("defaultOrder")
    @Expose
    public Integer defaultOrder;
    @SerializedName("nickname")
    @Expose
    public Integer nickname;
    @SerializedName("win")
    @Expose
    public Integer win;
    @SerializedName("loss")
    @Expose
    public Integer loss;
    @SerializedName("winPct")
    @Expose
    public Integer winPct;
    @SerializedName("gamesBehind")
    @Expose
    public Integer gamesBehind;
    @SerializedName("confWinLoss")
    @Expose
    public Integer confWinLoss;
    @SerializedName("divWinLoss")
    @Expose
    public Integer divWinLoss;
    @SerializedName("homeWinLoss")
    @Expose
    public Integer homeWinLoss;
    @SerializedName("awayWinLoss")
    @Expose
    public Integer awayWinLoss;
    @SerializedName("lastTenWinLoss")
    @Expose
    public Integer lastTenWinLoss;
    @SerializedName("streak")
    @Expose
    public Integer streak;

}
