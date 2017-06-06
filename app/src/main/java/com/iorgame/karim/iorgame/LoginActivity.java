package com.iorgame.karim.iorgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import customfonts.MyEditText;
import customfonts.MyRegulerText;
import cz.msebera.android.httpclient.Header;

/**
 * @author karim
 */
public class LoginActivity extends AppCompatActivity {

    private MyEditText email ;
    private MyEditText mdp;
    private MyRegulerText send;
    public static String PREFS_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (MyEditText)findViewById(R.id.email);
        mdp = (MyEditText)findViewById(R.id.mdp);
        send = (MyRegulerText)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    /**
     * private function that verify is email is in valid format
     * @param email in string format
     * @return true if email is in format xxxxxx@xxxxx.yyy, false if it's not in valid format
     */
    private static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * function that verify if password is in valid format
     * @param password in string format
     * @return true if password lenght is more then 5 carater, else false
     */
    private static boolean isValidPassword(String password) {
        return password.length()>=6;
    }

    /**
     * function that verify if email and password isn't empty and is in valid format
     * @return true if email and password is verified
     */
    private boolean verify(){
    boolean verified=true;
    if (email.getText().length()==0 ||mdp.getText().length()==0) {
        Toast.makeText(getApplicationContext(), "email/password must be defined", Toast.LENGTH_SHORT).show();
        verified=false;
    }else {
        System.err.println("valid"+isEmailValid(email.getText().toString()));
        System.err.println("valid"+isValidPassword(mdp.getText().toString()));

        if (!isEmailValid(email.getText().toString()) && !isValidPassword(mdp.getText().toString()) ){
            Toast.makeText(getApplicationContext(), "Invalid email/password", Toast.LENGTH_SHORT).show();
            verified=false;
        }
    }
    return verified;

}

    /**
     * function that send post http request to web service
     * and start niche activity if  login success
     */
    public void login() {
    if (verify()) {
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams paramss = new RequestParams();

        //Log.i("params",mEmail);
        paramss.put("email", email.getText());
        paramss.put("password", mdp.getText());

        //  192.168.0.11
        client.post(getApplicationContext(), "http://iorgame.herokuapp.com/public/api/auth/login", paramss, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Login Fail ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Toast.makeText(getApplicationContext(), "Login Success ", Toast.LENGTH_SHORT).show();
                Log.i("loginSuccess", responseString);
                System.err.println("loginSuccess " + responseString);
                if (responseString.contains("\"error\":false")) {

                    Intent intent = new Intent(LoginActivity.this, Reservation.class);
                    intent.putExtra("response", responseString);
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", mdp.getText().toString());


                    SharedPreferences sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    Editor editor = sharedpreferences.edit();
                    editor.putString("email", email.getText().toString());
                    editor.putString("password", mdp.getText().toString());
                    editor.commit();

                    startActivity(intent);

                } else
                    Toast.makeText(getApplicationContext(), "access denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
}
