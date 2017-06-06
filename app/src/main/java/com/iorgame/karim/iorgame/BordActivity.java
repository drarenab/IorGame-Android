package com.iorgame.karim.iorgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VerticalSeekBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class BordActivity extends AppCompatActivity {
    private Button on;
    private Button init;
    private Button off;


    WebView webview;
    RelativeLayout layout_joystick;
    JoyStickClass js;
/***********seekbar*************************/
    VerticalSeekBar verticalSeekBar=null;
    VerticalSeekBar verticalSeekBar1=null;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }
    private void openURL() {
        webview.loadUrl("http://iorgame.com/v/");
        webview.reload();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.requestFocus();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bord_layout);
        /**camera WebView***********************/
        webview=(WebView)findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());
        openURL();
        /*********Joystick**************************/
        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(0);



        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            final HashMap<String,String> param=new HashMap<String, String>();
            final Intent intent = getIntent();
           String Token=intent.getStringExtra("Token");
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);


                JSONObject obj = null;
                try {
                    obj = new JSONObject(Token);
                    String m_jArry = obj.getString("token");
                    System.err.println("mjarray"+m_jArry);
                    param.put("token",m_jArry);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String sens = null;

                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    //if(arg1.getAction() == MotionEvent.ACTION_UP){
                        int direction = js.get4Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        param.put("forward",String.valueOf(distanceToDegre(js.getDistance())));
                        //System.err.println("degre"+String.valueOf(distanceToDegre(js.getDistance())));
                        System.err.println("1");
                        sens="turn";
                        /*******************Avancer de Distance****************************/

                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        /*******************droite de Distance****************************/
                        param.put("turn",String.valueOf(distanceToDegre(js.getDistance())));
                        sens="forward";
                        System.err.println("2");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        /*******************reculer de Distance****************************/
                        param.put("forward",String.valueOf(distanceToDegre((js.getDistance())*-1)));
                        sens="turn";
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        /*******************gauche de Distance****************************/
                        param.put("turn",String.valueOf(distanceToDegre((js.getDistance())*-1)));
                        sens="forward";
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        /*******************Ne rien faire****************************/
                    System.err.println("5");
                        param.remove(sens);
                    }

                   // postRequest("https://iorgamews.herokuapp.com/",param);

                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {

                    /*****quand on enleve le doigt******************************/
                   // if(!param.isEmpty()&& param!=null)
                        param.remove(sens);
                        postRequest("https://iorgamews.herokuapp.com/",param);
                       // Toast.makeText(getApplicationContext(), "déplacer vers "+param.get("orientation")+" de "+param.get("degre")+ " pourcent", Toast.LENGTH_SHORT).show();

                }

                return true;
            }
        });
        /****************SeekBar****************************/
        /*
        Celle de gauche
         */
        verticalSeekBar=(VerticalSeekBar)findViewById(R.id.vertical_Seekbar);

        verticalSeekBar.setProgress(50);
        verticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final HashMap<String,String> param=new HashMap<String, String>();
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method
               // Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
               // Toast.makeText(getApplicationContext(),"start" , Toast.LENGTH_SHORT).show();
                System.err.println("startt");

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress,
                                          boolean fromUser) {
                //vsProgress.setText(progress+"");
               // Toast.makeText(getApplicationContext(),"progress" , Toast.LENGTH_SHORT).show();
                System.err.println("progress "+progress );
                seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction()==MotionEvent.ACTION_UP){
                            if(progress>50) {
                                param.put("orientation","open");
                            }
                            else  param.put("orientation","close");

                            param.put("degre", String.valueOf((progress%50)*2));
                            postRequest("url",param);
                        }


                            return false;

                    }

                });

            }
        });
        /*
        Celle de droite
         */
        verticalSeekBar1=(VerticalSeekBar)findViewById(R.id.vertical_Seekbar1);
        verticalSeekBar1.setProgress(50);
        verticalSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //vsProgress.setText(progress+"");

            }

        });
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

        client.get(getApplicationContext(),url , paramss, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "fail to send ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Toast.makeText(getApplicationContext(), "success to send ", Toast.LENGTH_SHORT).show();


                // Toast.makeText();


            }
        });

    }
    public String reloadImg()
    {
        Date date = new Date();
        return  "http://iotubo.univ-brest.fr/snapshot.cgi?user=visitor&pwd=trivial&t=" + Math.floor(date.getTime()/10);
    }


    /**
     *
     * @param distance
     * @return le pourcentage d'avancement du robot dans le sens indiqué
     */
    public float distanceToDegre(float distance){
        return (distance<180)?distance:180 ;
    }
   /* private DisplayMode calculateDisplayMode() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ?
                DisplayMode.FULLSCREEN : DisplayMode.BEST_FIT;
    }
    */


    @Override
    protected void onResume() {
        super.onResume();
        //Date date=new Date();

       // webview.loadUrl("http://iotubo.univ-brest.fr/snapshot.cgi?user=visitor&pwd=trivial&t="+(int)Math.floor(date.getTime()/10));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

