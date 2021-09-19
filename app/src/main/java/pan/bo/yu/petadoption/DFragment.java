package pan.bo.yu.petadoption;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.moos.library.HorizontalProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import static com.facebook.FacebookSdk.getApplicationContext;

public class DFragment extends Fragment {
    GoogleApiClient mGoogleApiClient;
    TextView nameText;
    Button signOut;
    ImageView imageView10;
    Button button_date;
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;
    AdRequest adRequest;

    //測試
    HorizontalProgressView horizontalProgressView;
    int i = 0;
    FrameLayout gameframe;
    SoundPool soundPool;
    int soundID;
    ImageView imageViewlove2;
    Handler handler;
    TextView text_add10, lV;
    public static int exp;
    public static int lvint;
    SharedPreferences.Editor editor;
    Button button_2;

    //獎勵廣告
    private RewardedAd mRewardedAd;

    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dfragment, container, false);
        return view;
    }

    //執行後內容 跟onCreate 差不多
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view  例如:textView =view.findViewById(R.id.text1);
        super.onViewCreated(view, savedInstanceState);


        nameText = view.findViewById(R.id.textView);
        signOut = view.findViewById(R.id.button22);
        imageView10 = view.findViewById(R.id.imageView10);
        button_date = view.findViewById(R.id.button_date);
        gameframe = view.findViewById(R.id.gameframe);
        imageViewlove2 = view.findViewById(R.id.love2);
        text_add10 = view.findViewById(R.id.text_add10);
        lV = view.findViewById(R.id.lV);
        horizontalProgressView = view.findViewById(R.id.progressView_horizontal);
        button_2 = view.findViewById(R.id.button_2);




        SharedPreferences preferences = this.getActivity().getSharedPreferences("pet", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //取得偏好姓名
        nameText.setText(preferences.getString("姓名key", ""));

        //如果沒有初始偏好設定  等級 經驗值 的話則產生
        if (preferences.getInt("等級key", 0) == 0) {
            editor.putInt("等級key", 1);
            editor.putInt("經驗值key", 10);
            editor.commit();
        }

        //取的偏好 等級 經驗值
        lvint = preferences.getInt("等級key", 0);
        exp = preferences.getInt("經驗值key", 0);

        //設置UI顯示等級及經驗值條
        lV.setText("" + lvint);
        horizontalProgressView.setProgress(exp);

        //取得偏好頭貼
//        Picasso.get().load(preferences.getString("頭貼key","")).into(imageView10);
        Glide.with(getActivity())
                .load(preferences.getString("頭貼key", ""))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(imageView10);


        //如果沒有初始偏好設定日期的話則產生
        if (preferences.getString("日期key", "").equals("")) {
            editor.putString("日期key", "0");
            editor.commit();
        }

        // 獲取今天日期
        Calendar mCal = Calendar.getInstance();
        String dateformat = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());

        //判斷今天是否已經簽到 的按鈕文本
        if (preferences.getString("日期key", "").equals(today)) {
            button_date.setText("今日已簽到");
        }

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("日期key", "").equals(today)) {
                    button_date.setText("今日已簽到");
                    //獎勵內容放這
                    Toast.makeText(getActivity(), "經驗值+300", Toast.LENGTH_SHORT).show();

                    lvup(30);

                    editor.putString("日期key", today);
                    editor.commit();
                }

            }
        });


        //登出按鈕
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("確定登出?");
                dialog.setMessage("登出後將會清除您的等級及經驗值");

                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        //這行是google登出
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {


                                        MainActivity.userID = null;
                                        MainActivity.editor.putString("姓名key", null);
                                        MainActivity.editor.putString("頭貼key", "https://miro.medium.com/max/743/1*II52xSQJ4RKcLwVMLKjgog.png");
                                        MainActivity.editor.putInt("等級key",1);
                                        MainActivity.editor.putInt("經驗值key",10);
                                        MainActivity.editor.putString("日期key","0");
                                        MainActivity.editor.commit();
                                        //FB登出code
                                        LoginManager.getInstance().logOut();

                                        Toast.makeText(getApplicationContext(), "已登出", Toast.LENGTH_SHORT).show();
                                        //跳到SignIUP登入畫面
                                        Intent i = new Intent(getApplicationContext(), SingUp.class);
                                        startActivity(i);
                                    }
                                });


                    }

                });


                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }

                });

                dialog.setCancelable(true);
                dialog.show();

            }

        }); //signOut End

        //橫幅廣告
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = view.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //橫幅廣告end

        //廣告加載
        loadRewardedAd();

        gemefood();

        //音效
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(getActivity(), R.raw.dogsound, 1);  //最後參數沒啥用 取1
        handler = new Handler();





        //觀看廣告按鈕
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRewardedAd != null) {
                    Activity activityContext = getActivity();
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Toast.makeText(getActivity(), "exp+800", Toast.LENGTH_SHORT).show();
                            lvup(80);

                            loadRewardedAd();
                        }
                    });
                }


            }
        });


    } //onViewCreated end


    //加載loadRewardedAd獎勵廣告
    public void loadRewardedAd(){

        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(getActivity(), "ca-app-pub-4133015411524559/6320420329",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mRewardedAd=null;
                        Log.d("result", loadAdError.getMessage());

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                        mRewardedAd=rewardedAd;
                        Log.d("result", "Ad was loaded.");
                    }
                });

    }
    public void gemefood() {
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageResource(R.drawable.hamburger);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(90, 90);
        params.topMargin = 50;
        params.leftMargin = 50;
        gameframe.addView(iv, params);

        //點選圖片
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rundom, rundom2;
                rundom = (int) (Math.random() * 5 + 1);
                switch (rundom) {
                    case 1:
                        iv.setImageResource(R.drawable.hamburger);
                        break;
                    case 2:
                        iv.setImageResource(R.drawable.dogfood0);
                        break;
                    case 3:
                        iv.setImageResource(R.drawable.dogfood1);
                        break;
                    case 4:
                        iv.setImageResource(R.drawable.dogfood2);
                        break;
                    case 5:
                        iv.setImageResource(R.drawable.dogfood3);
                        break;
                }
                rundom = (int) (Math.random() * 550 + 1);
                rundom2 = (int) (Math.random() * 500 + 1);
                params.setMargins(rundom, rundom2, 0, 0);
                iv.setLayoutParams(params);
                soundPool.play(soundID, 1, 1, 0, 0, 1);

                //愛心及顯示+經驗值
                imageViewlove2.setImageResource(R.drawable.love2);
                text_add10.setText("+10 exp");

                lvup(1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageViewlove2.setImageDrawable(null);
                        text_add10.setText("");
                    }
                }, 300);


            }
        });
    }

    //增加經驗值 參數是加的經驗值多寡
    public void lvup(int expp) {
        exp += expp;
        horizontalProgressView.setProgress(exp);
        if (exp >= 100) {
            exp -= 100;
            lvint++;
            lV.setText("" + lvint);
            horizontalProgressView.setProgress(exp);
            editor.putInt("等級key", lvint);
        }

        editor.putInt("經驗值key", exp);
        editor.commit();
    }


    //抓取你google帳戶的連結 作用是為了讓google登出
    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


}
