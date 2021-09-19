package pan.bo.yu.petadoption;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AFragment aFragment;
    private BFragment bFragment;
    private CFragment cFragment;
    private DFragment dFragment;

    private ImageView imageView1, imageView2, imageView3, imageView4;

    public static SharedPreferences.Editor editor;
    public static String userID, userUri;

    //AFragment
    public static int AFragment_sum=0;
    public static String [] AFragment1 ;
    public static String [] AFragment2 ;
    public static int count_AFrament1 = 1;
    public static int count_AFrament2 = 1;
    public static int count_AFrament3 = 1;
    public static String [] AFragment1_2 ;
    public static String [] AFragment1_3 ;


    //CFragment
    public static String[] CFragment1;
    public static int count_CFrament = 1;

    public static int dataInt =0;
    public static ArrayList  arrayName;
    public static ArrayList  arrayTell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light_NoActionBar);


        SharedPreferences sp = getSharedPreferences("pet", MODE_PRIVATE);

        editor = sp.edit();
        userID = sp.getString("姓名key", null);
        userUri = sp.getString("頭貼key", null);


        if (userID == null) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SingUp.class);
            startActivity(intent);
        }

        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);


        if (userID != null) {

            //放在這邊原因 第一次開啟時 登入畫面會閃一下本活動 所以先判斷是否有帳戶登入再載入本活動
            setContentView(R.layout.activity_main);

            imageView1 = findViewById(R.id.imageView1);
            imageView2 = findViewById(R.id.imageView2);
            imageView3 = findViewById(R.id.imageView3);
            imageView4 = findViewById(R.id.imageView4);

            //下面是Fragment
            aFragment = new AFragment();
            bFragment = new BFragment();
            cFragment = new CFragment();
            dFragment = new DFragment();

            //刷新A片段數據
            Refresh_AFragment();

            getFragmentManager().beginTransaction().add(R.id.aFramelayout, aFragment).commitAllowingStateLoss();
            pp(1);


            //刷新C片段數據
            Refresh_CFragment();


        }

    }

    public static void Refresh_AFragment(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("datatex_mom");
        DatabaseReference myRef2 = myRef.child("data_kid01");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AFragment_sum = (int) snapshot.getChildrenCount();
                AFragment1 = new String[AFragment_sum + 1];
                AFragment2 = new String[AFragment_sum + 1];
                AFragment1_2 = new String[AFragment_sum + 1];
                AFragment1_3 = new String[AFragment_sum + 1];


                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference storageRef_2 = FirebaseStorage.getInstance().getReference();
                StorageReference storageRef_3 = FirebaseStorage.getInstance().getReference();

                StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                //頭像
                for (int i = 1; i < AFragment_sum + 1; i++) {
                    int final2 = i;
                    storageRef2.child("headshot/headshot_" + i + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String str = uri.toString();
                            AFragment2[final2] = str;
                        }
                    });
                }

            //判斷三個照片是否都已經全部就緒
                count_AFrament1=1;
                count_AFrament2=1;
                count_AFrament3=1;
                //主照
                for (int i = 1; i < AFragment_sum + 1; i++) {

                    int finalI = i;
                    storageRef.child("pet_photo/pet_photo_" + i + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            AFragment1[finalI] = uri.toString();
                            Log.w("result", "載入第幾個pet主照:"+finalI);
                            //計數載入次數
                            count_AFrament1++;

                            //判斷是否全部家載完畢
                            if (count_AFrament1 == AFragment_sum + 1 && count_AFrament2==AFragment_sum+1 && count_AFrament3==AFragment_sum+1 ) {
                                AFragment.mRecyclerView.setAdapter(AFragment.myListAdapter);
                                AFragment.mRecyclerView.scrollToPosition(count_AFrament1-2);
                            }


                        }
                    });

                    storageRef_2.child("pet_photo/pet_photo_"+i+"_2.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AFragment1_2[finalI] = uri.toString();
                            count_AFrament2++;
                            //判斷是否全部家載完畢
                            if (count_AFrament1 == AFragment_sum + 1 && count_AFrament2==AFragment_sum+1 && count_AFrament3==AFragment_sum+1 ) {
                                AFragment.mRecyclerView.setAdapter(AFragment.myListAdapter);
                                AFragment.mRecyclerView.scrollToPosition(count_AFrament1-2);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AFragment1_2[finalI] ="空空的";
                        }
                    });

                    storageRef_3.child("pet_photo/pet_photo_"+finalI+"_3.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AFragment1_3[finalI] = uri.toString();
                            count_AFrament3++;

                            if (count_AFrament1 == AFragment_sum + 1 && count_AFrament2==AFragment_sum+1 && count_AFrament3==AFragment_sum+1 ) {
                                AFragment.mRecyclerView.setAdapter(AFragment.myListAdapter);
                                AFragment.mRecyclerView.scrollToPosition(count_AFrament1-2);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AFragment1_3[finalI] ="空空的";
                        }
                    });



                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public static void Refresh_CFragment(){


        arrayTell = new ArrayList();
        arrayName = new ArrayList();
        //預先載入聊天頭像
        CFragment1 = new String[40];
        count_CFrament =0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("datatex_mom");
        DatabaseReference myRef2 = myRef.child("data_kid02");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int id_sum = (int) snapshot.getChildrenCount();
                //抓取聊天頭貼 倒數40個

                StorageReference storRf = FirebaseStorage.getInstance().getReference();;

                id_sum-=39;

                for( int i = 0; i<40;i++){
                    int finalII = i;

                    storRf.child("tell_headshot/"+(id_sum+i)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.w("dddd","finalII"+finalII);
                            //這裡重要 影響了圖像是否錯亂載入的問題
                            int i = finalII;
                            CFragment1[i] = uri.toString();
                            Log.w("dddd","CFragment1[finalII]"+ CFragment1[i]);

                            //
                            //如果用  CFragment1[finalII] = uri.toString();
                            //在還沒生成uri.toString();的時候 finalII又被刷新 變更變量
                            //導致圖片錯亂 可能生成某個陣列裡面帶的變數不是正常的
                            //所以新增一個int 每次迴圈都是全新的 就算來不及加載 下個迴圈 又會產生新的int 不影響舊的INT
                            //


                            count_CFrament++;
                            Log.w("result","載入了多少次迴圈聊天頭像"+count_CFrament);
                            if(count_CFrament==40){
                                if(CFragment.CFragmant_int_x==1) {

//                                    CFragment.mRecyclerView.setAdapter(CFragment.recyclerLineAdapter);
                                    CFragment.CFragmant_int_x=0;
                                    }
                                Log.w("result","聊天for迴圈全部加載結束");

                            }
                        }
                    });
                }//抓取聊天頭貼 倒數40個 end

                //歷變 即時資料所有數據 name 跟 text
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //應該是映射Class 類別中code碼固定 他會自己帶入Return值 兩個String參數
                    //user_data就可以 點getz1()方法
                    //-- 白話文就是 user_data可以使用類別中getZ1()
                    TextString user_data = ds.getValue(TextString.class);

                    /*這裡邏輯是 因為每個ds都會歷遍一次
                     所以新增一個dataInt
                     迴圈到id_sum 總數是-40的所以 IF迴圈跑到我要的範圍就新增array資料
                     */
                    dataInt++;
                            if(dataInt>id_sum-1) {
                                arrayName.add(user_data.getZ2());
                                arrayTell.add(user_data.getZ1());
                            }

                }
                dataInt=0;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    } //Refresh_CFragment() end

    //跳轉Fragment的icon底色
    public void aFragment(View view) {
        pp(1);
        imageView1.setImageResource(R.drawable.home2);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout, aFragment).commitAllowingStateLoss();
    }

    public void bFragment(View view) {
        pp(2);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout, bFragment).commitAllowingStateLoss();
    }

    public void cFragment(View view) {
        pp(3);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout, cFragment).commitAllowingStateLoss();
    }

    public void dFragment(View view) {
        pp(4);
        getFragmentManager().beginTransaction().replace(R.id.aFramelayout, dFragment).commitAllowingStateLoss();
    }


    //跳轉Fragment
    public void pp(int fragnentImage) {
        imageView1.setImageResource(R.drawable.home1);
        imageView2.setImageResource(R.drawable.dog1);
        imageView3.setImageResource(R.drawable.line1);
        imageView4.setImageResource(R.drawable.user1);
        switch (fragnentImage) {
            case 1:
                imageView1.setImageResource(R.drawable.home2);
                break;
            case 2:
                imageView2.setImageResource(R.drawable.dog2);
                break;
            case 3:
                imageView3.setImageResource(R.drawable.line2);
                break;
            case 4:
                imageView4.setImageResource(R.drawable.user2);
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

    public void bww() {
        onDestroy();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }





}
