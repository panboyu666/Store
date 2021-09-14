package pan.bo.yu.petadoption;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;


public class AFragment extends Fragment {

    public static RecyclerView mRecyclerView;
    public static MyListAdapter myListAdapter;
    TextView textRegion, textRelease; //地區跟發布
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;

    //文本的內容下載地容器
    ArrayList<String> arrayList_text = new ArrayList();
    //姓名的下載地容器
    ArrayList<String> arrayList_name = new ArrayList();
    //地區的下載容器
    ArrayList<String> arrayList_region = new ArrayList();

    ArrayList array_like = new ArrayList();

    //下3行 firebaseRealtime //建立連線 //建立第一個母資料夾// 建立母資料夾的子資料夾
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("datatex_mom");
    DatabaseReference myRef2 = myRef.child("data_kid01");


    // 目的獲得總筆數
    int id_sum;


    //region選擇
    String region_string;
    TextView TextRegion;

    //進度條的布局
    public static LinearLayout linearLayout;

    //0911 text
    ViewPager2Adapter viewPager2Adapter;
    int get_position;
    ArrayList<String> arrayList ;


    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.afragment, container, false);
        return view;
    }

    //本身Fragment主方法
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textRegion = view.findViewById(R.id.TextRegion);
        textRelease = view.findViewById(R.id.TextRelease);


        linearLayout = view.findViewById(R.id.layoutppp);

        ProgressBar p2 = new ProgressBar(getActivity());
        linearLayout.addView(p2, 0);

        //下面是回收視圖碼
        mRecyclerView = view.findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        myListAdapter = new MyListAdapter();


        //下行是分隔線
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //以上回收視圖
        TextRegion = view.findViewById(R.id.TextRegion);


        //獲得總筆數 並把文章內容放在arrayList裡面
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id_sum = (int) snapshot.getChildrenCount();
                //dataSnapshot.getChildren() 是所有資料 但他用for 所以每個資料都會歷遍
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //應該是映射Class 類別中code碼固定 他會自己帶入Return值 兩個String參數
                    //user_data就可以 點getz1()方法
                    //-- 白話文就是 user_data可以使用類別中getZ1()
                    TextString user_data = ds.getValue(TextString.class);
                    //if目的防止每次開啟都重複新增
                    if (arrayList_text.size() != id_sum) {
                        arrayList_text.add(user_data.getZ1());
                        arrayList_name.add(user_data.getZ2());
                        arrayList_region.add(user_data.getZ3());
                        array_like.add(user_data.getZ4());
                    }

                }


                //放在這原因:讀取好總筆數再來顯示回收View 這樣才不會造成getItemCount 顯示組數沒辦法即時顯示

                mRecyclerView.setAdapter(myListAdapter);
                mRecyclerView.scrollToPosition(myListAdapter.getItemCount() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //地區按鈕
        textRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region();
            }
        });
        //發布按鈕
        textRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Release();
            }
        });

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


    }//create結束

    //選擇地區方法
    public void region() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(getActivity(), R.layout.dialoglayout, null);
                Button button1 = (Button) dialogView.findViewById(R.id.button1);
                Button button2 = (Button) dialogView.findViewById(R.id.button2);
                Button button3 = (Button) dialogView.findViewById(R.id.button3);
                Button button4 = (Button) dialogView.findViewById(R.id.button4);
                Button button5 = (Button) dialogView.findViewById(R.id.button5);
                Button button6 = (Button) dialogView.findViewById(R.id.button6);
                Button button7 = (Button) dialogView.findViewById(R.id.button7);
                Button button8 = (Button) dialogView.findViewById(R.id.button8);
                Button button9 = (Button) dialogView.findViewById(R.id.button9);
                Button button10 = (Button) dialogView.findViewById(R.id.button10);
                Button button11 = (Button) dialogView.findViewById(R.id.button11);
                Button button12 = (Button) dialogView.findViewById(R.id.button12);
                Button button13 = (Button) dialogView.findViewById(R.id.button13);
                Button button14 = (Button) dialogView.findViewById(R.id.button14);
                Button button15 = (Button) dialogView.findViewById(R.id.button15);
                Button button16 = (Button) dialogView.findViewById(R.id.button16);
                Button button17 = (Button) dialogView.findViewById(R.id.button17);
                Button button18 = (Button) dialogView.findViewById(R.id.button18);
                Button button19 = (Button) dialogView.findViewById(R.id.button19);
                Button button20 = (Button) dialogView.findViewById(R.id.button20);
                Button button21 = (Button) dialogView.findViewById(R.id.button21);
                dialog.setView(dialogView);
                dialog.show();

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Button繼承自TextView,而TextView又繼承自View,所以可以向下轉型
                        Button btn = (Button) v;
                        //Button一般都有一個Id(佈局文件中設定)，用以判斷到底點擊的是哪一個
                        switch (btn.getId()) {
                            case R.id.button1:
                                region_string = "台北市";
                                break;
                            case R.id.button2:
                                region_string = "新北市";
                                break;
                            case R.id.button3:
                                region_string = "桃園市";
                                break;
                            case R.id.button4:
                                region_string = "台中市";
                                break;
                            case R.id.button5:
                                region_string = "台南市";
                                break;
                            case R.id.button6:
                                region_string = "高雄市";
                                break;
                            case R.id.button7:
                                region_string = "新竹縣";
                                break;
                            case R.id.button8:
                                region_string = "苗栗縣";
                                break;
                            case R.id.button9:
                                region_string = "彰化縣";
                                break;
                            case R.id.button10:
                                region_string = "南投縣";
                                break;
                            case R.id.button11:
                                region_string = "雲林縣";
                                break;
                            case R.id.button12:
                                region_string = "嘉義縣";
                                break;
                            case R.id.button13:
                                region_string = "屏東縣";
                                break;
                            case R.id.button14:
                                region_string = "宜蘭縣";
                                break;
                            case R.id.button15:
                                region_string = "花蓮縣";
                                break;
                            case R.id.button16:
                                region_string = "台東縣";
                                break;
                            case R.id.button17:
                                region_string = "澎湖縣";
                                break;
                            case R.id.button18:
                                region_string = "金門縣";
                                break;
                            case R.id.button19:
                                region_string = "連江縣";
                                break;
                            case R.id.button20:
                                region_string = "基隆市";
                                break;
                            case R.id.button21:
                                region_string = "全區";
                                break;
                        }
                        dialog.dismiss();
                        TextRegion.setText("目前選擇的地區是:" + region_string);

                    }
                }; //listener end

                button1.setOnClickListener(listener);
                button2.setOnClickListener(listener);
                button3.setOnClickListener(listener);
                button4.setOnClickListener(listener);
                button5.setOnClickListener(listener);
                button6.setOnClickListener(listener);
                button7.setOnClickListener(listener);
                button8.setOnClickListener(listener);
                button9.setOnClickListener(listener);
                button10.setOnClickListener(listener);
                button11.setOnClickListener(listener);
                button12.setOnClickListener(listener);
                button13.setOnClickListener(listener);
                button14.setOnClickListener(listener);
                button15.setOnClickListener(listener);
                button16.setOnClickListener(listener);
                button17.setOnClickListener(listener);
                button18.setOnClickListener(listener);
                button19.setOnClickListener(listener);
                button20.setOnClickListener(listener);
                button21.setOnClickListener(listener);

            }
        }, 0);


    }

    //選擇發布方法
    public void Release() {
        Intent intent = new Intent(getActivity(), Release.class);
        startActivity(intent);
    }

    //回收View
    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {


        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView1, imageView4;
            private TextView text1, text2, text3, liketext;

            private ViewPager2 pager2;
            private CircleIndicator3 indicator3;


            //這裡綁定ID 注意綁定語法不一樣  finID前面需要加 super(itemView)的內容 itemView
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                text1 = itemView.findViewById(R.id.text1);
                text2 = itemView.findViewById(R.id.text2);
                text3 = itemView.findViewById(R.id.text3);
                imageView1 = itemView.findViewById(R.id.imageView1);
                imageView4 = itemView.findViewById(R.id.imageView4);
                liketext = itemView.findViewById(R.id.like00);

                pager2 = itemView.findViewById(R.id.viewpager2_66);
                indicator3 = itemView.findViewById(R.id.indicator);


            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyle_item, parent, false);
            return new ViewHolder(view);
        }

        @Override  //從這裡面變更設定內容 引用holder.裡面 finID的資料修改
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (MainActivity.count_AFrament == MainActivity.AFragment_sum + 1) {
                arrayList= new ArrayList<>();
                arrayList.add(MainActivity.AFragment1[position+1]);
                arrayList.add(MainActivity.AFragment1_2[position+1]);
                arrayList.add(MainActivity.AFragment1_3[position+1]);

                viewPager2Adapter = new ViewPager2Adapter(arrayList);
                holder.pager2.setAdapter(viewPager2Adapter);
                holder.indicator3.setViewPager(holder.pager2);
            }


            holder.imageView1.setImageResource(R.drawable.ic_loading2);

            holder.text1.setText(arrayList_name.get(position));
            holder.text2.setText(arrayList_text.get(position));
            holder.text3.setText(arrayList_region.get(position));

            holder.liketext.setText(array_like.get(position).toString());



            //設定頭像載入最後一張的時候 取消進度條
            Glide.with(getActivity()).load(MainActivity.AFragment2[position + 1])
                    .placeholder(R.drawable.ic_loading2)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);

                        }

                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            //頭像
                            holder.imageView1.setImageDrawable(resource);
                            linearLayout.removeAllViews();

                        }
                    });

            //按讚功能
            holder.imageView4.setOnClickListener(new View.OnClickListener() {
                int limit = 0;

                @Override
                public void onClick(View v) {
                    limit++;
                    if (limit < 6) {
                        int i = Integer.parseInt(holder.liketext.getText().toString());
                        i++;
                        holder.liketext.setText(i + "");
                        DatabaseReference myRef3 = database.getReference("datatex_mom/data_kid01/" + (position + 1) + "/z4");
                        myRef3.setValue(i);
                    }
                    if (limit > 5) {
                        if (limit < 7) {
                            Toast.makeText(getActivity(), "謝謝，已到達限制", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

        }

        @Override   //顯示條列
        public int getItemCount() {
            return id_sum;
        }

    }

    public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.inView> {
        private ArrayList<String> arrayList;

        ViewPager2Adapter(ArrayList<String> arrayList) {
            this.arrayList = arrayList;
        }

        class inView extends RecyclerView.ViewHolder {

            private ImageView imageView;

            public inView(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.viewpager2_image);


            }

        }

        @NonNull
        @Override
        public ViewPager2Adapter.inView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewPager2Adapter.inView(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.viewpager2, parent, false
                    )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPager2Adapter.inView holder, int position) {

            Log.w("result", "三小");

                if (position == 0) {
                    holder.imageView.setImageResource(R.drawable.load_66);
                    Glide.with(getActivity()).load(arrayList.get(position)).placeholder(R.drawable.ic_loading).into(holder.imageView);

                }
                if (position == 1) {

                    holder.imageView.setImageResource(R.drawable.load_66);
                    Glide.with(getActivity()).load(arrayList.get(position)).placeholder(R.drawable.load_66).into(holder.imageView);
                }
                if (position == 2) {
                    holder.imageView.setImageResource(R.drawable.load_66);
                    Glide.with(getActivity()).load(arrayList.get(position)).placeholder(R.drawable.load_66).into(holder.imageView);
                }


        }


        @Override
        public int getItemCount() {
            return 3;
        }


    }


}