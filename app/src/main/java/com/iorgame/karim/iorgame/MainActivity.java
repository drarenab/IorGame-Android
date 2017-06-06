package com.iorgame.karim.iorgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

/**
 * @author karim
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
            Attente de 2 secondes et faire apparaitre l'interface d'authentification
         */
        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish(){
                System.out.println("lancement de l'interface login");
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                //Intent intent = new Intent(MainActivity.this,Reservation.class);
                //Intent intent = new Intent(MainActivity.this,Reservation.class);

              // Intent intent = new Intent(MainActivity.this,BordActivity.class);

                startActivity(intent);
            }

        }.start();


    }

}
