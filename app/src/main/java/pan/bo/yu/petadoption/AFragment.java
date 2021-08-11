package pan.bo.yu.petadoption;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AFragment extends Fragment {

    RecyclerView mRecyclerView;
    TextView textRegion,textRelease; //地區跟發布
    //下兩行橫幅廣告
    ClipboardManager clipboard;
    AdView mAdView;

    ArrayList<String> arrayList = new ArrayList();



    //下3行 firebaseRealtime //建立連線 //建立第一個母資料夾// 建立母資料夾的子資料夾
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("datatex_mom");
    DatabaseReference myRef2 = myRef.child("data_kid01");
    // 目的獲得總筆數
    int id_sum;



    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.afragment, container, false );
        return view;
    }

    //本身Fragment主方法
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        textRegion =view.findViewById( R.id.TextRegion );
        textRelease=view.findViewById( R.id.TextRelease );
        //下面是回收視圖碼
        mRecyclerView = view.findViewById( R.id.recycleview );
        mRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        //下行是分隔線
        mRecyclerView.addItemDecoration( new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL ) );
       //以上回收視圖


            //獲得總筆數 並把文章內容放在arrayList裡面
            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    id_sum= (int)snapshot.getChildrenCount();
                    Log.w("result","id_sum以讀取"+id_sum);
                    //dataSnapshot.getChildren() 是所有資料 但他用for 所以每個資料都會歷遍
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //應該是映射Class 類別中code碼固定 他會自己帶入Return值 兩個String參數
                        //user_data就可以 點getz1()方法
                        //-- 白話文就是 user_data可以使用類別中getZ1()
                        TextString user_data = ds.getValue(TextString.class);

                            if(arrayList.size()!=id_sum) {
                                arrayList.add(user_data.getZ1());
                            }

                    }


                    //放在這原因:讀取好總筆數再來顯示回收View 這樣才不會造成getItemCount 顯示組數沒辦法即時顯示
                    mRecyclerView.setAdapter( new MyListAdapter());

                }
                @Override
                public void onCancelled(@NonNull  DatabaseError error) {
                }});



//
//        //獲得總圖片 並把圖片放在arrayList裡面
//
//        //連線到FirebaseStorage
//        storageReference =FirebaseStorage.getInstance().getReference();
//        Log.w("result","id_sum"+id_sum);
//        for(int i=1 ; i<id_sum+1 ;i++){
//
//        pic_storage = storageReference.child("3.jpg");
//        try {
//            //創建臨時文件suffix
//            file = File.createTempFile("image","jpg");
//            //添加成功後的事件
//            pic_storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
////                    ImageView image = new ImageView(getActivity());
////                    image.setImageURI(Uri.fromFile(file));
////                    arrayListimage.add(image);
//
//                    arrayOO.add(Uri.fromFile(file));
//                    Log.w("result","已增加的"+arrayListimage);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.w("result","已吃雞雞的"+e);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.w("result","已損害的"+e);
//        }
//        }
        //以上storage



        //地區按鈕
        textRegion.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region();
            }
        } );
        //發布按鈕
        textRelease.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Release();
            }
        } );

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
    //選擇地區方法
    public void region(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(getActivity(), R.layout.dialoglayout, null);
                dialog.setView(dialogView);
                dialog.show();
            }
        },0);

    }

    //選擇發布方法
    public void Release(){
        Intent intent = new Intent(getActivity(),Release.class);
        startActivity(intent);
    }

    //回收View
     public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{

            private ImageView imageView3;
            private TextView text1,text2 ;

            //這裡綁定ID 注意綁定語法不一樣  finID前面需要加 super(itemView)的內容 itemView
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                text1 = itemView.findViewById(R.id.text1);
                text2 = itemView.findViewById(R.id.text2);
                imageView3= itemView.findViewById(R.id.imageView3);


            }}

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyle_item,parent,false);
            return new ViewHolder(view);
        }

        @Override  //從這裡面變更設定內容 引用holder.裡面 finID的資料修改
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.text2.setText(arrayList.get(position));


            StorageReference storageRef =FirebaseStorage.getInstance().getReference();
            storageRef.child(position+1+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getActivity())
                            .load(uri)
                            .resize(250, 1000)   // 將圖片寬高轉為200*200 pixel
                            .centerInside()
                            .into(holder.imageView3);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            


//            holder.tvId.setText(arrayList.get(position).get("Id"));
//            holder.tvSub1.setText(arrayList.get(position).get("Sub1"));
//            holder.tvSub2.setText(arrayList.get(position).get("Sub2"));
        }


        @Override   //顯示條列
        public int getItemCount() {
            return id_sum;
        }

    }


}