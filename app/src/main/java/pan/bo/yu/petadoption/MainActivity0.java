package pan.bo.yu.petadoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;



public class MainActivity0 extends AppCompatActivity {

    public static  SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static String userID,userUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        sp=getSharedPreferences("pet",MODE_PRIVATE);
        editor = sp.edit();

        userID = sp.getString("姓名key",null);
        userUri= sp.getString("頭貼key",null);

        if(userID==null){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity0.this,SingUp.class);
                    startActivity(intent);

                    finish();
                }
            },1000);
        }

        if(userID!=null){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity0.this,MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            },1000);
        }




    }
}