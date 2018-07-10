package com.ankuringale.footballlive.football;

import com.ankuringale.footballlive.R;

import java.io.Serializable;

public class Event implements Serializable{

    private String teamCode;
    private String player;
    private String type;
    private String time;
    private int colorEvent;

    public Event(String teamCode, String player, String type, String time) {
        this.teamCode = teamCode;
        this.player = player;
        this.type = type;
        this.time = time;

        switch(type)
        {
            case "YELLOW CARD":
                this.colorEvent = R.color.yellow;
                break;
            case "RED CARD":
                this.colorEvent = R.color.red;
            default:
                this.colorEvent = R.color.green;
        }
    }

    public String getTeamCode() {
        return teamCode;
    }


    public String getPlayer() {
        return player.split(" ")[1];
    }

    public int getColorEvent()
    {
        return colorEvent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return getTime(time);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public   String getTime(String type)
    {
        switch (type)
        {
            case "full-time":
                return "Full Time";
            case "half-time":
                return "Half Time";
            default:
                return type;
        }

    }
}
