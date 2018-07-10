package com.ankuringale.footballlive.football;

import android.util.Log;

import com.ankuringale.footballlive.ui.CurrentScoreActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Matches implements Serializable{

    private String jsonData;
    private transient JSONObject currentMatch;
    private transient JSONArray array;
    private boolean currentSettingsDone;
    private List<Current> previous;

    public Matches() {
        currentSettingsDone = false;
        previous = new ArrayList<>();
    }


    public void getJSON(String jsonData) throws JSONException {
        this.jsonData = jsonData;
        array = new JSONArray(jsonData);
    }

    public Current returnCurrentMatch() throws JSONException {
        for (int i = array.length() - 1; i >= 0; i--) {
            JSONObject mainObject = array.getJSONObject(i);
            if (mainObject.get("time") != null && !mainObject.getString("time").equals("null")) {
                currentMatch = mainObject;
                break;
            }
        }
        Current c = new Current();
        c.getJSONObject(currentMatch);
        c.setup();
        return c;
    }

    public void setPreviousMatches() throws JSONException {
        boolean x = false;
        previous.clear();
        for (int i = array.length() - 1; i >= 0; i--) {
            JSONObject mainObject = array.getJSONObject(i);
            if (mainObject.get("time") != null && !mainObject.getString("time").equals("null")) {
                if(x) {
                    Current current = new Current();
                    current.getJSONObject(mainObject);
                    current.setup(x);
                    previous.add(current);
                }
                else
                    x = true;
            }
        }
    }

    public List<Current> getPrevious() {

        Collections.sort(previous, new Comparator<Current>() {

            @Override
            public int compare(Current o1, Current o2) {

              String d1 = o1.getDate();
              String d2 = o2.getDate();
              try {
                  SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");

                  if (d1.length() == 4) {
                      d1 = d1.substring(0, 2) + "-0" + d1.substring(3) + "-2018";

                      Log.v(CurrentScoreActivity.class.getSimpleName() , d1);
                  }

                  if (d1.length() == 3) {
                      d1 = "0" + d1.substring(0, 1) + "-0" + d1.substring(2) + "-2018";
                      Log.v(CurrentScoreActivity.class.getSimpleName() , d1);
                  }

                  if (d2.length() == 4) {
                      d2 = d2.substring(0, 2) + "-0" + d2.substring(3) + "-2018";
                      Log.v(CurrentScoreActivity.class.getSimpleName() , d2);
                  }

                  if (d2.length() == 3) {
                      d2 = "0" + d2.substring(0, 1) + "-0" + d2.substring(2) + "-2018";
                      Log.v(CurrentScoreActivity.class.getSimpleName() , d2);
                  }


                  Date date1 = f.parse(d1);
                  Date date2 = f.parse(d2);
                  if (date1.after(date2)) {
                      return -1;
                  }

                  if (date1.before(date2)) {
                      return 1;
                  }

                  if (date1.equals(date2)) {
                      return 0;
                  }

                  return 0;
              }
              catch(ParseException e)
              {
                  return 0;
              }
            }


        });



        Log.v(CurrentScoreActivity.class.getSimpleName() , previous.get(0).getDate());
        return previous;
    }

    public int toInt (String s)
    {
        return Integer.parseInt(s);
    }
}
