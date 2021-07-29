package pan.bo.yu.petadoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

import pan.bo.yu.store.R;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    AppCompatActivity appCompatActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    //把參數變成本類別的值
    public MyProductAdapter(AppCompatActivity appCompatActivity,List<SkuDetails> skuDetails,BillingClient billingClient){
        this.appCompatActivity = appCompatActivity;
        this.skuDetailsList = skuDetails;
        this.billingClient = billingClient;
    }



    //內部類 主要是要綁ID的
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_product_name,txt_price,txt_dascription;
        //這是接口
        IRcyclerClickListener iRcyclerClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_dascription =itemView.findViewById(R.id.txt_dascription);
            txt_product_name =itemView.findViewById(R.id.txt_product_name);
            txt_price =itemView.findViewById(R.id.txt_price);

            //itemView設定了Click的事件this 意義不明  會不會是點擊Recycler就觸發?
            itemView.setOnClickListener(this);
        }

        //有一個方法 名字是 設定頃聽者 有參數  參數是接口型別 這個方法調用會在onBindViewHolder的時候
        public void setListener(IRcyclerClickListener listener){
            this.iRcyclerClickListener = listener;
        }

        //這個是實現接口
        @Override
        public void onClick(View view){
            iRcyclerClickListener.onClick(view,getAdapterPosition());
        }
    } //內部類



    //設定布局是哪一個帶入
    @NonNull
    @Override
    public MyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(appCompatActivity.getBaseContext())
                .inflate(R.layout.layout_product_display,parent,false));
    }


    //主要onBindViewHolder 設置功能區
    @Override
    public void onBindViewHolder(@NonNull  MyProductAdapter.MyViewHolder holder, int position) {
        holder.txt_product_name.setText(skuDetailsList.get(position).getTitle());
        holder.txt_dascription.setText(skuDetailsList.get(position).getDescription());
        holder.txt_price.setText(skuDetailsList.get(position).getPrice());

        holder.setListener(new IRcyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(position))
                        .build();
                int reponse = billingClient.launchBillingFlow(appCompatActivity,billingFlowParams)
                        .getResponseCode();
                switch (reponse){
                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        Toast.makeText(appCompatActivity, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Toast.makeText(appCompatActivity, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(appCompatActivity, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Toast.makeText(appCompatActivity, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        Toast.makeText(appCompatActivity, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        Toast.makeText(appCompatActivity, "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Toast.makeText(appCompatActivity, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        });



    }

    //回傳組
    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }
}
