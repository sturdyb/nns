
package com.example.takomar.nospoilersnba.component.Retro.Standings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standing {

    @SerializedName("teamId")
    @Expose
    public String teamId;
    @SerializedName("win")
    @Expose
    public String win;
    @SerializedName("loss")
    @Expose
    public String loss;
    @SerializedName("winPct")
    @Expose
    public String winPct;
    @SerializedName("winPctV2")
    @Expose
    public String winPctV2;
    @SerializedName("lossPct")
    @Expose
    public String lossPct;
    @SerializedName("lossPctV2")
    @Expose
    public String lossPctV2;
    @SerializedName("gamesBehind")
    @Expose
    public String gamesBehind;
    @SerializedName("divGamesBehind")
    @Expose
    public String divGamesBehind;
    @SerializedName("clinchedPlayoffsCode")
    @Expose
    public String clinchedPlayoffsCode;
    @SerializedName("clinchedPlayoffsCodeV2")
    @Expose
    public String clinchedPlayoffsCodeV2;
    @SerializedName("confRank")
    @Expose
    public String confRank;
    @SerializedName("confWin")
    @Expose
    public String confWin;
    @SerializedName("confLoss")
    @Expose
    public String confLoss;
    @SerializedName("divWin")
    @Expose
    public String divWin;
    @SerializedName("divLoss")
    @Expose
    public String divLoss;
    @SerializedName("homeWin")
    @Expose
    public String homeWin;
    @SerializedName("homeLoss")
    @Expose
    public String homeLoss;
    @SerializedName("awayWin")
    @Expose
    public String awayWin;
    @SerializedName("awayLoss")
    @Expose
    public String awayLoss;
    @SerializedName("lastTenWin")
    @Expose
    public String lastTenWin;
    @SerializedName("lastTenLoss")
    @Expose
    public String lastTenLoss;
    @SerializedName("streak")
    @Expose
    public String streak;
    @SerializedName("divRank")
    @Expose
    public String divRank;
    @SerializedName("isWinStreak")
    @Expose
    public Boolean isWinStreak;
    @SerializedName("tieBreakerPts")
    @Expose
    public String tieBreakerPts;
    @SerializedName("sortKey")
    @Expose
    public SortKey sortKey;

}
