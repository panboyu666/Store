package pan.bo.yu.petadoption;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class CFragment extends Fragment {
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;
    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.cfragment,container,false);
        return view;
    }

    //執行後內容 跟onCreate 差不多
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view
        //textView =view.findViewById(R.id.text1);

        super.onViewCreated(view, savedInstanceState);

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
    }

}
