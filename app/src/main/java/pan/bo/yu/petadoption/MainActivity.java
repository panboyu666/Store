package pan.bo.yu.petadoption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



import java.io.File;




public class MainActivity extends AppCompatActivity {

    private AFragment aFragment;
    private BFragment bFragment;
    private CFragment cFragment;
    private DFragment dFragment;

    private ImageView imageView1,imageView2,imageView3,imageView4;



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


    //跳轉Fragment的icon底色
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


    //跳轉Fragment
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

    //離開提示
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// 按下鍵盤上返回按鈕
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("確定退出APP嗎？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    public void bww(){
        onDestroy();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}