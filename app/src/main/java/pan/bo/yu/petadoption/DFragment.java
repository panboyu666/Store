package pan.bo.yu.petadoption;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import pan.bo.yu.store.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DFragment extends Fragment {
    GoogleApiClient mGoogleApiClient;
    TextView nameText;
    Button signOut;
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
        //finID前面要加view
        //textView =view.findViewById(R.id.text1);
        super.onViewCreated(view, savedInstanceState);

        nameText =view.findViewById(R.id.textView);
        nameText.setText(""+MainActivity0.userID);
        signOut =view.findViewById(R.id.button22);

//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity0.userID=null;
//                MainActivity0.editor.putString("姓名key",null);
//                MainActivity0.editor.commit();
//
//
//                Intent intent = new Intent(getActivity(),SingUp.class);
//                startActivity(intent);
//
//            }
    //登出按鈕
      signOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
         Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                 new ResultCallback<Status>() {
                   @Override
                   public void onResult(Status status) {

                       MainActivity0.userID=null;
                       MainActivity0.editor.putString("姓名key",null);
                       MainActivity0.editor.commit();
                       LoginManager.getInstance().logOut();

                       Toast.makeText(getApplicationContext(),"已登出",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),SingUp.class);
                        startActivity(i);
                                }
                            });
                }}); //signOut End

        }

    //這個應該是抓取你帳戶的連結
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
