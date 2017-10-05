
package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Bd {

    @SerializedName("b")
    @Expose
    private List<B> b = null;

    public List<B> getB() {
        return b;
    }

    public void setB(List<B> b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("b", b).toString();
    }

}
