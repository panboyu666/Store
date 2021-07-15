package pan.bo.yu.store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    AFragment.MyListAdapter myListAdapter;
    String [] sx= new String[13];
    ArrayList<String> arrayList = new ArrayList();

    ArrayList<ImageView> arrayList22 = new ArrayList<>();
    String[] sURL = new String[]{"https://images.goodsmile.info/cgm/images/product/20190925/8841/64026/large/83d0954b6bca2fdbdf736aa44387f4dd.jpg","https://i.pinimg.com/236x/10/99/0b/10990b52f7b7f8f02bbcd067ab9f1d8c.jpg"};

    private AFragment aFragment;
    private BFragment bFragment;
    private CFragment cFragment;
    private DFragment dFragment;

    private ImageView imageView1,imageView2,imageView3,imageView4;

    int fragnentImage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1=findViewById( R.id.imageView1 );
        imageView2=findViewById( R.id.imageView2 );
        imageView3=findViewById( R.id.imageView3);
        imageView4=findViewById( R.id.imageView4);

        //下面是Fragment
        aFragment = new AFragment();
        bFragment = new BFragment();
        cFragment = new CFragment();
        dFragment = new DFragment();
        getFragmentManager().beginTransaction().add(R.id.aFramelayout,aFragment).commitAllowingStateLoss();
        pp(1);
    }

    public void trunRelease(View view ){
        Intent intent = new Intent(this,Release.class);
        startActivity(intent);

    }

    public void aFragment(View view ){
        pp(1);
        imageView1.setImageResource( R.drawable.home2 );
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout,aFragment).commitAllowingStateLoss();
    }
    public void bFragment(View view ){
        pp(2);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout,bFragment).commitAllowingStateLoss();
    }
    public void cFragment(View view ){
        pp(3);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout,cFragment).commitAllowingStateLoss();
    }
    public void dFragment(View view ){
        pp(4);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout,dFragment).commitAllowingStateLoss();
    }

    public void pp2(View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(MainActivity.this, R.layout.dialoglayout, null);
                dialog.setView(dialogView);
                dialog.show();
            }
        },100);

    }

    public void pp(int fragnentImage){
        imageView1.setImageResource( R.drawable.home1 );
        imageView2.setImageResource( R.drawable.dog1 );
        imageView3.setImageResource( R.drawable.line1 );
        imageView4.setImageResource( R.drawable.user1 );
        switch(fragnentImage) {
            case 1:
                imageView1.setImageResource( R.drawable.home2 );
                break;
            case 2:
                imageView2.setImageResource( R.drawable.dog2 );
                break;
            case 3:
                imageView3.setImageResource( R.drawable.line2 );
                break;
            case 4:
                imageView4.setImageResource( R.drawable.user2 );
                break;

        }


    }





}