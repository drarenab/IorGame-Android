package com.iorgame.karim.iorgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.iorgame.karim.iorgame.LoginActivity.PREFS_NAME;

/**
 * Created by karim on 29/05/2017.
 */

public class Reservation extends ReservationChreno {
    public List<WeekViewEvent> events;
    private final ArrayList<Chreno> booking=new ArrayList<>();

    boolean b=false;
    private String emaiString;
   private String passString;
    @Override
    public List<? extends WeekViewEvent> onMonthChange(final int newYear, final int newMonth) {
        // Populate the week view with some events.

        events = new ArrayList<WeekViewEvent>();

        final Intent intent = getIntent();
        emaiString=intent.getStringExtra("email");
        passString=intent.getStringExtra("password");
        booking.addAll(loadJSONFromAsset(intent.getStringExtra("response")));
        if (events!=null) {
            if (events.size() > 0) {
                events.clear();
            }
        }
         for (int i=0;i<booking.size();i++) {


             {
                 Calendar startTime = Calendar.getInstance();

             //Heur
             startTime.set(Calendar.HOUR_OF_DAY,booking.get(i).getHour_start());
             //minute
             startTime.set(Calendar.MINUTE, 0);
             //Mois

                startTime.set(Calendar.MONTH, booking.get(i).getMonth()-1);//booking.get(i).getMonth());

             //Année
             startTime.set(Calendar.YEAR,booking.get(i).getYear());//booking.get(i).getYear());
             //Jour
             startTime.set(Calendar.DAY_OF_MONTH,booking.get(i).getDay());

             Calendar endTime = (Calendar) startTime.clone();
             endTime.add(Calendar.HOUR_OF_DAY,1);
             //endTime.set(Calendar.MONTH, Integer.parseInt(jma[1]));
             WeekViewEvent event = new WeekViewEvent(1, null/*getEventTitle(startTime)*/, startTime, endTime);
                 if(booking.get(i).getIreserveIt()==0)
                     event.setColor(getResources().getColor(R.color.ownReserved));
                 else
                     event.setColor(getResources().getColor(R.color.reservedChreno));
                 events.add(event);
             }

         }
        useRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //maitre a jour booking
                bookingMAJ();

                //verifier l'acces avec le serveur
                final HashMap<String,String> param=new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                param.put("email",settings.getString("email",""));
                param.put("password",settings.getString("password",""));


                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams paramss = new RequestParams();

                paramss.put("email",settings.getString("email",""));
                paramss.put("password",settings.getString("password",""));

                client.post(getApplicationContext(),"http://iorgame.herokuapp.com/public/api/auth/loginToken" , paramss, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "havn't access", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        System.err.println("resonseString"+responseString);
                        Toast.makeText(getApplicationContext(), "Access to Board", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Reservation.this, BordActivity.class);
                        intent.putExtra("Token", responseString);
                        startActivity(intent);

                    }
                });


               // postRequest("https://iorgamews.herokuapp.com/public/api/auth/logginToken",param);

            }
        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Calendar startTime = Calendar.getInstance();

                //Heur
                startTime.set(Calendar.HOUR_OF_DAY,time.getTime().getHours());
                //minute
                startTime.set(Calendar.MINUTE, 0);
                //Mois

                startTime.set(Calendar.MONTH, time.getTime().getMonth()-1);//booking.get(i).getMonth());

                //Année
                startTime.set(Calendar.YEAR,time.getTime().getYear()+1900);//booking.get(i).getYear());
                //Jour
                startTime.set(Calendar.DAY_OF_MONTH,time.getTime().getDate());

                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY,1);
                //endTime.set(Calendar.MONTH, Integer.parseInt(jma[1]));
                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.accent));
                events.add(event);
                Toast.makeText(getApplicationContext(),String.valueOf(time.getTime().getMonth()) , Toast.LENGTH_SHORT).show();

                HashMap<String,String> param=new HashMap<String, String>();

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                param.put("email",settings.getString("email","not exists"));//intent.getStringExtra("email"));
                param.put("password",settings.getString("password","not exists"));//intent.getStringExtra("password"));
                param.put("year", String.valueOf(time.getTime().getYear()+1900));
                param.put("month",String.valueOf(time.getTime().getMonth()+1));
                param.put("day",String.valueOf(time.getTime().getDate()));
                param.put("hour",String.valueOf(time.getTime().getHours()));

                postRequest("http://iorgame.herokuapp.com/public/api/bookADate",param);
                bookingMAJ();


            }
        });
        return events;
    }


    public ArrayList<Chreno> loadJSONFromAsset(String response) {
        ArrayList<Chreno> chrenoList = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(response);

            JSONArray m_jArry = obj.getJSONArray("bookings");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Chreno chreno = new Chreno();
                String date=jo_inside.getString("date").split(" ")[0];//contient JMA
                String date1=jo_inside.getString("date").split(" ")[1];//contient HMS
                chreno.setDay(Integer.parseInt(date.split("-")[2]));
                chreno.setMonth(Integer.parseInt(date.split("-")[1]));
                chreno.setYear(Integer.parseInt(date.split("-")[0]));
                chreno.setHour_start(Integer.parseInt(date1.split(":")[0]));
                chreno.setHour_end((Integer.parseInt(date1.split(":")[0]))+1);
                chreno.setIreserveIt(Integer.parseInt(jo_inside.getString("user_id")));
                //Add your values in your `ArrayList` as below:
                chrenoList.add(chreno);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chrenoList;
    }

    private void postRequest(String url,HashMap<String,String> param){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams paramss = new RequestParams();

        //faire une boucle pour tous les parametres avec une hasmap

        for (Map.Entry<String, String> entry : param.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            paramss.put(key, value);

        }
        client.post(getApplicationContext(),url , paramss, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "fail to send ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Toast.makeText(getApplicationContext(), "success to send ", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());



            }
        });

    }
    public void bookingMAJ() {

            AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams paramss = new RequestParams();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            paramss.put("email", settings.getString("email","not exists"));
            paramss.put("password", settings.getString("password","not exists"));



            client.post(getApplicationContext(), "http://iorgame.herokuapp.com/public/api/auth/login", paramss, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Login Fail ", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                      if (responseString.contains("\"error\":false")) {
                        booking.clear();
                        booking.addAll(loadJSONFromAsset(responseString));


                    } else
                        Toast.makeText(getApplicationContext(), "access denied", Toast.LENGTH_SHORT).show();


                }
            });


    }
}
