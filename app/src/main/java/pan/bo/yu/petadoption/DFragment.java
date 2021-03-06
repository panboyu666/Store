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
    //?????????????????????
    ClipboardManager clipboard;
    AdView mAdView;
    AdRequest adRequest;

    //??????
    HorizontalProgressView horizontalProgressView;
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

    //????????????
    private RewardedAd mRewardedAd;

    //????????????Xml??????View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dfragment, container, false);
        return view;
    }

    //??????????????? ???onCreate ?????????
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID????????????view  ??????:textView =view.findViewById(R.id.text1);
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
        //??????????????????
        nameText.setText(preferences.getString("??????key", ""));

        //??????????????????????????????  ?????? ????????? ???????????????
        if (preferences.getInt("??????key", 0) == 0) {
            editor.putInt("??????key", 1);
            editor.putInt("?????????key", 10);
            editor.commit();
        }

        //???????????? ?????? ?????????
        lvint = preferences.getInt("??????key", 0);
        exp = preferences.getInt("?????????key", 0);

        //??????UI???????????????????????????
        lV.setText("" + lvint);
        horizontalProgressView.setProgress(exp);

        //??????????????????
//        Picasso.get().load(preferences.getString("??????key","")).into(imageView10);
        Glide.with(getActivity())
                .load(preferences.getString("??????key", ""))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(imageView10);


        //???????????????????????????????????????????????????
        if (preferences.getString("??????key", "").equals("")) {
            editor.putString("??????key", "0");
            editor.commit();
        }

        // ??????????????????
        Calendar mCal = Calendar.getInstance();
        String dateformat = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());

        //?????????????????????????????? ???????????????
        if (preferences.getString("??????key", "").equals(today)) {
            button_date.setText("???????????????");
        }

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("??????key", "").equals(today)) {
                    button_date.setText("???????????????");
                    //??????????????????
                    Toast.makeText(getActivity(), "?????????+300", Toast.LENGTH_SHORT).show();

                    lvup(30);

                    editor.putString("??????key", today);
                    editor.commit();
                }

            }
        });


        //????????????
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("?????????????");
                dialog.setMessage("?????????????????????????????????????????????");

                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        //?????????google??????
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {


                                        MainActivity.userID = null;
                                        MainActivity.editor.putString("??????key", null);
                                        MainActivity.editor.putString("??????key", "https://miro.medium.com/max/743/1*II52xSQJ4RKcLwVMLKjgog.png");
                                        MainActivity.editor.putInt("??????key",1);
                                        MainActivity.editor.putInt("?????????key",10);
                                        MainActivity.editor.putString("??????key","0");
                                        MainActivity.editor.commit();
                                        //FB??????code
                                        LoginManager.getInstance().logOut();

                                        Toast.makeText(getApplicationContext(), "?????????", Toast.LENGTH_SHORT).show();
                                        //??????SignIUP????????????
                                        Intent i = new Intent(getApplicationContext(), SingUp.class);
                                        startActivity(i);
                                    }
                                });


                    }

                });


                dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }

                });

                dialog.setCancelable(true);
                dialog.show();

            }

        }); //signOut End

        //????????????
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = view.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //????????????end

        //????????????
        loadRewardedAd();

        gemefood();

        //??????
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(getActivity(), R.raw.dogsound, 1);  //????????????????????? ???1
        handler = new Handler();





        //??????????????????
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
                if(mRewardedAd==null){
                    Toast.makeText(getActivity(), "?????????,????????????????????????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    loadRewardedAd();

                }

            }
        });


    } //onViewCreated end


    //??????loadRewardedAd????????????
    public void loadRewardedAd(){

        AdRequest adRequest2 = new AdRequest.Builder().build();
//       ??????ID ca-app-pub-3940256099942544/5224354917
        RewardedAd.load(getActivity(), "ca-app-pub-4133015411524559/6320420329",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mRewardedAd=null;
                        Log.d("aaa","????????????????????????:" +loadAdError.getMessage());

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                        mRewardedAd=rewardedAd;
                        Log.d("aaa", "????????????????????????Ad was loaded.");
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






        //????????????
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

                //???????????????+?????????
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

    //??????????????? ??????????????????????????????
    public void lvup(int expp) {
        exp += expp;
        horizontalProgressView.setProgress(exp);
        if (exp >= 100) {
            exp -= 100;
            lvint++;
            lV.setText("" + lvint);
            horizontalProgressView.setProgress(exp);
            editor.putInt("??????key", lvint);
        }

        editor.putInt("?????????key", exp);
        editor.commit();
    }


    //?????????google??????????????? ??????????????????google??????
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
