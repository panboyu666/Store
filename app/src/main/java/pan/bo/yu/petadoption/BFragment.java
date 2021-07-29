package pan.bo.yu.petadoption;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.Arrays;
import java.util.List;

import pan.bo.yu.store.R;

public class BFragment extends Fragment implements PurchasesUpdatedListener{
    BillingClient billingClient;
    ConsumeResponseListener consumeResponselistener;
    RecyclerView recyclerView;
    TextView textView2;
    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bfragment,container,false);
        return view;
    }

    //執行後內容 跟onCreate 差不多
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view
        //textView =view.findViewById(R.id.text1);
        super.onViewCreated(view, savedInstanceState);

        setupBillingClient();

        recyclerView= view.findViewById(R.id.recycler_product);
        textView2 = view.findViewById(R.id.textView2);

        //以下recyclerView的基礎設置

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),linearLayoutManager.getOrientation()));
        //

        //讀取Play內購的產品 需手動新增產品ID
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean flage =true;
                while(flage) {
                    if (billingClient.isReady()) {
                        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                                .setSkusList(Arrays.asList("donate", "donate2", "donate3", "donate4"))
                                .setType(BillingClient.SkuType.INAPP)
                                .build();
                        billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, List<SkuDetails> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                                    loadProductToRecyclerView(list);

                            }
                        });
                        flage=false;
                    }

                }
            }
        },50);




    }




    private void loadProductToRecyclerView(List<SkuDetails> list) {
        MyProductAdapter adapter = new MyProductAdapter( (AppCompatActivity) getActivity(),list,billingClient);
        recyclerView.setAdapter(adapter);

    }

    //初始化2 關於BillingClient
    private void setupBillingClient() {

        //暫定為購買成功後的事件
        consumeResponselistener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull  String s) {

                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK)
                    Toast.makeText(getActivity(), "感謝您的愛心", Toast.LENGTH_SHORT).show();

            }
        };
        //設置獲取實例
        billingClient = BillingClientSetup.getInstance(getActivity(),this);

        //開始連接
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK)
                {

                    //集合裡面 放進去查詢採購 再放進去類型INAPP 在.獲取採購清單
                    //目前理解是購買後的商品清單會在這裡
                    Log.w("result","購買清單"+billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList());

                    List<Purchase> listPurchases =billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList();
                    //有了購買清單後 處理這些訊息
                    handleItemAlreadyPuchase(listPurchases);
                }
                else
                    Toast.makeText(getActivity(), "Error code:"+
                            billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();

            }
            //斷開伺服器
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getActivity(), "You are disconnect from Billing Service 斷開伺服器", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //簡單的說是顯示購買紀錄 但我不知道為什麼用到IF下的內容
    //if內容 是獲取令牌唷 ComsumeParams 這個是參數 用在billingClient.consumeAsync
    private void handleItemAlreadyPuchase(List<Purchase> purchases) {
        StringBuilder purchasedItem = new StringBuilder(textView2.getText());

        for(Purchase purchase : purchases)
        {
            if(purchase.getSku().equals("donate"))
            {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.consumeAsync(consumeParams, consumeResponselistener);
            }
            purchasedItem.append("\n"+purchase.getSku())
                    .append("\n");

        }

        textView2.setText(purchasedItem.toString());
        textView2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult,  List<Purchase> list) {

    }



}
