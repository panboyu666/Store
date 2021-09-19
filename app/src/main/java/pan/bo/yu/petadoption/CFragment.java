package pan.bo.yu.petadoption;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class CFragment extends Fragment {
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;
    //data_kid02資料的總數
    int id_sum;
    //名字容器
    ArrayList arrayName;
    //文字內容 容器
    ArrayList arrayTell;
    //輸入框
    EditText edittext_text;
    //發送鈕
    Button button_port;
    //偏好 頭貼 & 名字
    String headshotUri;
    String headshotName;

    //布局為了新增進度條
    RelativeLayout relativelayou_c;

    //回收& 配置器
    public static RecyclerView mRecyclerView;
    public static RecyclerLineAdapter recyclerLineAdapter;

    // 即時資料
    FirebaseDatabase database;
    DatabaseReference dr;
    DatabaseReference dr2;
    // 圖檔庫
    StorageReference storRf;
    StorageReference hradshot_storage;

    //目的 刷新訊息時 Main抓取資料 作用
    public static int CFragmant_int_x =0;

    //返回配置Xml文件View
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cfragment, container, false);
        return view;
    }


    //執行後內容 跟onCreate 差不多
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view  例如:textView =view.findViewById(R.id.text1);
        super.onViewCreated(view, savedInstanceState);

        CFragmant_int_x=1;

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

        edittext_text = view.findViewById(R.id.edittext_text);
        button_port = view.findViewById(R.id.button_port);
        mRecyclerView = view.findViewById(R.id.recycleline);
        relativelayou_c=view.findViewById(R.id.relativelayou_c);

        button_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button_potr();

            }
        });

        database = FirebaseDatabase.getInstance();
        dr = database.getReference("datatex_mom");
        dr2 = dr.child("data_kid02");

        storRf = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("pet", 0);
        //得到偏好中的姓名
        headshotName = preferences.getString("姓名key", "");
        //得到偏好中的頭貼uri_String
        headshotUri = preferences.getString("頭貼key", "");

        //設置Manger
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        //設置Manger
        mManager.setStackFromEnd(true); //用於軟鍵盤時 recycle會頂上去
        //設置Manger
        mRecyclerView.setLayoutManager(mManager);



        //避免啟動就切聊天 回收數組是空的會抱錯  跟旋轉進度條 6秒後重載
        if(MainActivity.CFragment1[39]==null) {
            ProgressBar progressBar = new ProgressBar(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 120);
            params.topMargin = 0;
            params.leftMargin = 470;
            relativelayou_c.addView(progressBar, params);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(MainActivity.CFragment1[39]!=null){
                        relativelayou_c.removeView(progressBar);
                        //實現Adapter物件
                        recyclerLineAdapter = new RecyclerLineAdapter(MainActivity.CFragment1,MainActivity.arrayName,MainActivity.arrayTell);
                        mRecyclerView.setAdapter(recyclerLineAdapter);
                    }
                    else{
                        Toast.makeText(getActivity(), "請重新刷新頁面", Toast.LENGTH_SHORT).show();
                    }
                }
            },6000);

        }else {
            //實現Adapter物件
            recyclerLineAdapter = new RecyclerLineAdapter(MainActivity.CFragment1,MainActivity.arrayName,MainActivity.arrayTell);
            mRecyclerView.setAdapter(recyclerLineAdapter);
        }




    }//onCreate end


   private void refresh() {

            MainActivity.Refresh_CFragment();

            }


    public void Button_potr() {



        if(!headshotName.equals("訪客")){

            CFragmant_int_x =1;
            dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    id_sum = (int) snapshot.getChildrenCount();
                    id_sum += 1;
                    //上傳編輯框內容跟 名字
                    dr2.child(String.valueOf(id_sum)).setValue(new TextString(edittext_text.getText().toString(), headshotName));

                    //秒添加布局
                    recyclerLineAdapter.addItem(edittext_text.getText().toString());
                    //上傳hradshot
                    hradshot_storage = storRf.child("tell_headshot/" + id_sum + ".jpg");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputStream innn = new URL(headshotUri).openStream();
                                hradshot_storage.putStream(innn).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            innn.close();
                                           // refresh(); 刷新方法
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (IOException e) {
                                Toast.makeText(getActivity(), "很抱歉出錯了"+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    edittext_text.setText("");
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            Toast.makeText(getActivity(), "訪客不能留言唷 請登入GOOGLE帳戶或FB帳戶", Toast.LENGTH_SHORT).show();
        }

    } //button end

    //內部類Adapter
    private class RecyclerLineAdapter extends RecyclerView.Adapter<RecyclerLineAdapter.ViewHolder> {
        String[] str_array;
        ArrayList arrayName,arrayTell;


        public RecyclerLineAdapter(String[] aFragment1, ArrayList arrayName, ArrayList arrayTell) {
            this.str_array=aFragment1;
            this.arrayName=arrayName;
            this.arrayTell=arrayTell;

        }



        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imagename;
            private TextView textname, texttell;

            //這裡綁定ID 注意綁定語法不一樣  finID前面需要加 super(itemView)的內容 itemView
            public ViewHolder(View itemView) {
                super(itemView);
                imagename = itemView.findViewById(R.id.imagename);
                textname = itemView.findViewById(R.id.textname);
                texttell = itemView.findViewById(R.id.texttell);


            }
        }

        @NonNull
        @Override   //帶入布局
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_line_layout, parent, false);
            return new ViewHolder(view);


        }

        @Override  //從這裡面變更設定內容 引用holder.裡面 finID的資料修改
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.imagename.setImageResource(R.drawable.ic_loading2);


            holder.textname.setText(arrayName.get(position).toString());
            holder.texttell.setText(arrayTell.get(position).toString());



            if(position<str_array.length){

                   Glide.with(getActivity())
                           .load(str_array[position])
                           .apply(RequestOptions
                           .bitmapTransform(new RoundedCorners(30)))
                           .into(holder.imagename);

            }
            //目的 使用者自己打字的時候新增
            if(position>=str_array.length){
                Glide.with(getActivity())
                        .load(headshotUri)
                        .apply(RequestOptions
                                .bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imagename);
            }

        }

        @Override   //顯示條列
        public int getItemCount() {
            return arrayName.size();


        }

        public void addItem( String Tell){
            arrayName.add(headshotName);
            arrayTell.add(Tell);
            notifyDataSetChanged();
            mRecyclerView.scrollToPosition(getItemCount() - 1);
        }

    }





}

