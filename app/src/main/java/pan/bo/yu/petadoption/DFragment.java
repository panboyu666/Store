package pan.bo.yu.petadoption;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;



import static com.facebook.FacebookSdk.getApplicationContext;

public class DFragment extends Fragment {
    GoogleApiClient mGoogleApiClient;
    TextView nameText;
    Button signOut;
    ImageView imageView10;
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;
    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dfragment,container,false);
        return view;
    }

    //執行後內容 跟onCreate 差不多
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view  例如:textView =view.findViewById(R.id.text1);
        super.onViewCreated(view, savedInstanceState);

        nameText =view.findViewById(R.id.textView);
        signOut =view.findViewById(R.id.button22);
        imageView10=view.findViewById(R.id.imageView10);


        SharedPreferences preferences = this.getActivity().getSharedPreferences("pet", Context.MODE_PRIVATE);

        //取得偏好姓名
        nameText.setText(preferences.getString("姓名key",""));

        //取得偏好頭貼
        Picasso.with(getApplicationContext()).load(preferences.getString("頭貼key","")).into(imageView10);


    //登出按鈕
      signOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //這行是google登出
         Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                 new ResultCallback<Status>() {
                   @Override
                   public void onResult(Status status) {

                       MainActivity0.userID=null;
                       MainActivity0.editor.putString("姓名key",null);
                       MainActivity0.editor.putString("頭貼key",null);
                       MainActivity0.editor.commit();
                       //FB登出code
                       LoginManager.getInstance().logOut();

                       Toast.makeText(getApplicationContext(),"已登出",Toast.LENGTH_SHORT).show();
                       //跳到SignIUP登入畫面
                        Intent i = new Intent(getApplicationContext(),SingUp.class);
                        startActivity(i);
                                }
                            });
                }}); //signOut End

        //橫幅廣告
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //橫幅廣告end

        } //onViewCreated end

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
