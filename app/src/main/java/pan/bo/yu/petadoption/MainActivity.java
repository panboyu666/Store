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

        Log.w("aaa","??????1");

        SharedPreferences sp = getSharedPreferences("pet", MODE_PRIVATE);

        editor = sp.edit();
        userID = sp.getString("??????key", null);
        userUri = sp.getString("??????key", null);


        if (userID == null) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SingUp.class);
            startActivity(intent);
        }

        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);

        Log.w("aaa","userID??????2"+userID);
        if (userID != null) {

            Log.w("aaa","??????2");
            //?????????????????? ?????????????????? ????????????????????????????????? ??????????????????????????????????????????????????????
            setContentView(R.layout.activity_main);

            imageView1 = findViewById(R.id.imageView1);
            imageView2 = findViewById(R.id.imageView2);
            imageView3 = findViewById(R.id.imageView3);
            imageView4 = findViewById(R.id.imageView4);

            //?????????Fragment
            aFragment = new AFragment();
            bFragment = new BFragment();
            cFragment = new CFragment();
            dFragment = new DFragment();

            //??????A????????????
            Refresh_AFragment();

            getFragmentManager().beginTransaction().add(R.id.aFramelayout, aFragment).commitAllowingStateLoss();
            pp(1);


            //??????C????????????
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
                //??????
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

            //?????????????????????????????????????????????
                count_AFrament1=1;
                count_AFrament2=1;
                count_AFrament3=1;
                //??????
                for (int i = 1; i < AFragment_sum + 1; i++) {

                    int finalI = i;
                    storageRef.child("pet_photo/pet_photo_" + i + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            AFragment1[finalI] = uri.toString();
                            Log.w("result", "???????????????pet??????:"+finalI);
                            //??????????????????
                            count_AFrament1++;

                            //??????????????????????????????
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
                            //??????????????????????????????
                            if (count_AFrament1 == AFragment_sum + 1 && count_AFrament2==AFragment_sum+1 && count_AFrament3==AFragment_sum+1 ) {
                                AFragment.mRecyclerView.setAdapter(AFragment.myListAdapter);
                                AFragment.mRecyclerView.scrollToPosition(count_AFrament1-2);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AFragment1_2[finalI] ="?????????";
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
                            AFragment1_3[finalI] ="?????????";
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
        //????????????????????????
        CFragment1 = new String[40];
        count_CFrament =0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("datatex_mom");
        DatabaseReference myRef2 = myRef.child("data_kid02");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int id_sum = (int) snapshot.getChildrenCount();
                //?????????????????? ??????40???

                StorageReference storRf = FirebaseStorage.getInstance().getReference();;

                id_sum-=39;

                for( int i = 0; i<40;i++){
                    int finalII = i;

                    storRf.child("tell_headshot/"+(id_sum+i)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.w("dddd","finalII"+finalII);
                            //???????????? ??????????????????????????????????????????
                            int i = finalII;
                            CFragment1[i] = uri.toString();
                            Log.w("dddd","CFragment1[finalII]"+ CFragment1[i]);

                            //
                            //?????????  CFragment1[finalII] = uri.toString();
                            //???????????????uri.toString();????????? finalII???????????? ????????????
                            //?????????????????? ?????????????????????????????????????????????????????????
                            //??????????????????int ??????????????????????????? ????????????????????? ???????????? ??????????????????int ???????????????INT
                            //


                            count_CFrament++;
                            Log.w("result","????????????????????????????????????"+count_CFrament);
                            if(count_CFrament==40){
                                if(CFragment.CFragmant_int_x==1) {

//                                    CFragment.mRecyclerView.setAdapter(CFragment.recyclerLineAdapter);
                                    CFragment.CFragmant_int_x=0;
                                    }
                                Log.w("result","??????for????????????????????????");

                            }
                        }
                    });
                }//?????????????????? ??????40??? end

                //?????? ???????????????????????? name ??? text
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //???????????????Class ?????????code????????? ??????????????????Return??? ??????String??????
                    //user_data????????? ???getz1()??????
                    //-- ??????????????? user_data?????????????????????getZ1()
                    TextString user_data = ds.getValue(TextString.class);

                    /*??????????????? ????????????ds??????????????????
                     ??????????????????dataInt
                     ?????????id_sum ?????????-40????????? IF????????????????????????????????????array??????
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

    //??????Fragment???icon??????
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


    //??????Fragment
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

    //????????????
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// ???????????????????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("????????????APP??????")
                    .setNegativeButton("??????",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("??????",
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
