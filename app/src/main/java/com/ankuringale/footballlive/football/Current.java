package com.ankuringale.footballlive.football;

import android.nfc.Tag;
import android.util.Log;

import com.ankuringale.footballlive.R;
import com.ankuringale.footballlive.ui.CurrentScoreActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;


public class Current implements Serializable {
    private String team1Score;
    private String team2Score;
    private String team1Penalty;
    private String team2Penalty;
    transient private JSONObject currentMatch;

    public String getTeam1Penalty() {
        return team1Penalty;
    }

    public void setTeam1Penalty(String team1Penalty) {
        this.team1Penalty = team1Penalty;
    }

    public String getTeam2Penalty() {
        return team2Penalty;
    }

    public void setTeam2Penalty(String team2Penalty) {
        this.team2Penalty = team2Penalty;
    }

    private String date;
    private String weather;
    private String status;
    private List<Event> events;
    private String time;
    private String stageName;
    private String team1Code;
    private String team2Code;

    public String getTeam1Code() {
        return team1Code;
    }

    public void setTeam1Code(String team1Code) {
        this.team1Code = team1Code;
    }

    public String getTeam2Code() {
        return team2Code;
    }

    public void setTeam2Code(String team2Code) {
        this.team2Code = team2Code;
    }


    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Current() {
    }

    public Current(String team1Penalty, String team2Penalty, String team1Score, String team2Score,
                   String date, String weather, String status,
                   String time, String stageName, String team1Code, String team2Code, List<Event> events) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.team1Penalty = team1Penalty;
        this.team2Penalty = team2Penalty;
        this.events = events;
        this.date = date;
        this.weather = weather;
        this.status = status;
        this.time = time;
        this.stageName = stageName;
        this.team1Code = team1Code;
        this.team2Code = team2Code;
    }

    public void getJSONObject(JSONObject currentMatch) {
        this.currentMatch = currentMatch;
        events = new ArrayList<>();
    }

    public String getTeam1Score() {

        if (team1Penalty != null || team2Penalty != null) {
            if (team1Score.equals(team2Score) && !(team1Penalty.equals("0") || team2Penalty.equals("0")))
                return team1Score + "(" + team1Penalty + ")";
            else
                return team1Score;
        } else
            return team1Score;
    }

    public void setTeam1Score(String team1Score) {
        this.team1Score = team1Score;
    }

    public String getTeam2Score() {
        if (team1Penalty != null || team2Penalty != null) {
            if (team1Score.equals(team2Score) && (toInt(team1Penalty) != 0 || toInt(team2Penalty) != 0))
                return team2Score + "(" + team2Penalty + ")";
            else
                return team2Score;
        } else
            return team2Score;
    }

    public void setTeam2Score(String team2Score) {
        this.team2Score = team2Score;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setup() throws JSONException {

        events.clear();

        JSONObject root = currentMatch;

        if (root != null) {
            Log.v(TAG, root.getString("stage_name"));
            JSONObject weatherObject = root.getJSONObject("weather");
            String t1c, t2c;


            this.setWeather(weatherObject.getString("description"));
            this.setStageName(root.getString("stage_name"));


            String myDate = root.getString("datetime");
            this.setDate(toInt(myDate.substring(8, 10)) + "/" + toInt(myDate.substring(5, 7)));
            this.setStatus(getStatus(root.getString("status")));
            this.setTime(getTime(root.getString("time")));

            JSONObject team = root.getJSONObject("home_team");
            t1c = team.getString("code");
            this.setTeam1Code(t1c);
            this.setTeam1Penalty("" + team.getInt("penalties"));
            this.setTeam1Score("" + team.getInt("goals"));


            team = root.getJSONObject("away_team");

            t2c = team.getString("code");
            this.setTeam2Code(t2c);
            this.setTeam2Penalty("" + team.getInt("penalties"));
            this.setTeam2Score("" + team.getInt("goals"));


            JSONArray teamEvents = root.getJSONArray("home_team_events");

            for (int i = 0; i < teamEvents.length(); i++) {
                team = teamEvents.getJSONObject(i);
                String type = team.getString("type_of_event");
                //about to add events
                String player = team.getString("player");
                String eTime = team.getString("time");

                if (typeOfEvent(type) != null)
                    events.add(new Event(t1c, player, typeOfEvent(type), eTime));
            }

            teamEvents = root.getJSONArray("away_team_events");

            for (int i = 0; i < teamEvents.length(); i++) {
                team = teamEvents.getJSONObject(i);
                String type = team.getString("type_of_event");
                //about to add events
                String player = team.getString("player");
                String eTime = team.getString("time");

                if (typeOfEvent(type) != null)
                    events.add(new Event(t2c, player, typeOfEvent(type), eTime));
            }

            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    String t1 = o1.getTime();
                    String t2 = o2.getTime();
                    int tt1 = 0;
                    int tt2 = 0;

                    Log.v(TAG , t1);
                    if(t1.charAt(t1.length()-3) != '+')
                    {
                        tt1 = toInt(t1.substring(0, t1.length() - 1));
                    }

                    else
                    {
                        if(t1.length() == 6)
                            tt1 = toInt(t1.substring(0,2)) + toInt(t1.substring(4,5));

                        if(t1.length() == 7)
                            tt1 = toInt(t1.substring(0,3)) + toInt(t1.substring(5,6));

                    }

                    if(t2.charAt(t2.length()-3) != '+')
                        tt2 = toInt(t2.substring(0, t2.length() - 1));
                    else
                    {
                        if(t2.length() == 6)
                            tt2 = toInt(t2.substring(0,2)) + toInt(t2.substring(4,5));
                        if(t2.length() == 7)
                            tt2 = toInt(t2.substring(0,3)) + toInt(t2.substring(5,6));
                    }

                    return tt2 - tt1;
                }
            });
        }

    }


    public void setup(boolean justForOverloading) throws JSONException {
        JSONObject root = currentMatch;

        if (root != null) {
            Log.v(TAG, root.getString("stage_name"));

            String t1c, t2c;



            String myDate = root.getString("datetime");
            this.setDate(toInt(myDate.substring(8, 10)) + "/" + toInt(myDate.substring(5, 7)));

            JSONObject team = root.getJSONObject("home_team");
            t1c = team.getString("code");

            this.setTeam1Code(t1c);

            this.setTeam1Penalty("" + team.getInt("penalties"));
            this.setTeam1Score("" + team.getInt("goals"));


            team = root.getJSONObject("away_team");

            t2c = team.getString("code");

            this.setTeam2Code(t2c);
            this.setTeam2Penalty("" + team.getInt("penalties"));
            this.setTeam2Score("" + team.getInt("goals"));

        }
    }

        public int toInt (String s)
        {
            return Integer.parseInt(s);
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


    public  String typeOfEvent(String type) {
        switch(type)
        {
            case "goal-own":
                return "O GOAL";
            case "goal-penalty":
                return "PENALTY";
            case "goal":
                return "GOAL";
            case "yellow-card":
                return "YELLOW CARD";
            case "red-card":
                return "RED CARD";
            default :
                return null;
        }
    }

    public  String getStatus(String type)
    {
        String status;

        switch(type)
        {
            case "completed":
                status = "Well Played!";
                break;
            case "in progress":
                status= "Match Is Live!";
                break;
            default:
                status="Guess Something's Wrong!";
                break;
        }

        return status;
    }

}
