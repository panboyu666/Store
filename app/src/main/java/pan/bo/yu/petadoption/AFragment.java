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
    TextView textRegion, textRelease; //???????????????
    //?????????????????????
    ClipboardManager clipboard;
    AdView mAdView;

    //??????????????????????????????
    ArrayList<String> arrayList_text = new ArrayList();
    //????????????????????????
    ArrayList<String> arrayList_name = new ArrayList();
    //?????????????????????
    ArrayList<String> arrayList_region = new ArrayList();
    //
    ArrayList array_like = new ArrayList();
    //?????? ?????? ?????? ?????? ?????? ???????????????
    ArrayList<String> arrayList_animal = new ArrayList<>();
    ArrayList<String> arrayList_phone = new ArrayList<>();
    ArrayList<String> arrayList_gender = new ArrayList<>();
    ArrayList<String> arrayList_ligation = new ArrayList<>();
    ArrayList<String> arrayList_vaccine = new ArrayList<>();

    //???3??? firebaseRealtime //???????????? //???????????????????????????// ?????????????????????????????????
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("datatex_mom");
    DatabaseReference myRef2 = myRef.child("data_kid01");


    // ?????????????????????
    int id_sum;


    //region??????
    String region_string;
    TextView TextRegion;

    //??????????????????
    public static LinearLayout linearLayout;

    //0911 text
    ViewPager2Adapter viewPager2Adapter;
    int get_position;
    ArrayList<String> arrayList;


    //????????????Xml??????View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.afragment, container, false);
        return view;
    }

    //??????Fragment?????????
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //??????
//        textRegion = view.findViewById(R.id.TextRegion);
        textRelease = view.findViewById(R.id.TextRelease);

        linearLayout = view.findViewById(R.id.layoutppp);

        ProgressBar p2 = new ProgressBar(getActivity());
        linearLayout.addView(p2, 0);

        //????????????????????????
        mRecyclerView = view.findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        myListAdapter = new MyListAdapter();


        //??????????????????
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //??????????????????
        //??????
//        TextRegion = view.findViewById(R.id.TextRegion);


        //??????????????? ????????????????????????arrayList??????
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id_sum = (int) snapshot.getChildrenCount();
                //dataSnapshot.getChildren() ??????????????? ?????????for ??????????????????????????????
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //???????????????Class ?????????code????????? ??????????????????Return??? ??????String??????
                    //user_data????????? ???getz1()??????
                    //-- ??????????????? user_data?????????????????????getZ1()
                    TextString user_data = ds.getValue(TextString.class);
                    //if???????????????????????????????????????
                    if (arrayList_text.size() != id_sum) {
                        arrayList_text.add(user_data.getZ1());
                        arrayList_name.add(user_data.getZ2());
                        arrayList_region.add(user_data.getZ3());
                        array_like.add(user_data.getZ4());
                        arrayList_animal.add(user_data.getZ5());
                        arrayList_phone.add(user_data.getZ6());
                        arrayList_gender.add(user_data.getZ7());
                        arrayList_ligation.add(user_data.getZ8());
                        arrayList_vaccine.add(user_data.getZ9());
                    }

                }


                //???????????????:????????????????????????????????????View ?????????????????????getItemCount ?????????????????????????????????

                mRecyclerView.setAdapter(myListAdapter);
                mRecyclerView.scrollToPosition(myListAdapter.getItemCount() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //????????????
//        textRegion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                region();
//            }
//        });

        //????????????
        textRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Release();
            }
        });

        //????????????
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //????????????end


    }//create??????

    //??????????????????
//    public void region() {
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                final AlertDialog dialog = builder.create();
//                View dialogView = View.inflate(getActivity(), R.layout.dialoglayout, null);
//                Button button1 = (Button) dialogView.findViewById(R.id.button1);
//                Button button2 = (Button) dialogView.findViewById(R.id.button2);
//                Button button3 = (Button) dialogView.findViewById(R.id.button3);
//                Button button4 = (Button) dialogView.findViewById(R.id.button4);
//                Button button5 = (Button) dialogView.findViewById(R.id.button5);
//                Button button6 = (Button) dialogView.findViewById(R.id.button6);
//                Button button7 = (Button) dialogView.findViewById(R.id.button7);
//                Button button8 = (Button) dialogView.findViewById(R.id.button8);
//                Button button9 = (Button) dialogView.findViewById(R.id.button9);
//                Button button10 = (Button) dialogView.findViewById(R.id.button10);
//                Button button11 = (Button) dialogView.findViewById(R.id.button11);
//                Button button12 = (Button) dialogView.findViewById(R.id.button12);
//                Button button13 = (Button) dialogView.findViewById(R.id.button13);
//                Button button14 = (Button) dialogView.findViewById(R.id.button14);
//                Button button15 = (Button) dialogView.findViewById(R.id.button15);
//                Button button16 = (Button) dialogView.findViewById(R.id.button16);
//                Button button17 = (Button) dialogView.findViewById(R.id.button17);
//                Button button18 = (Button) dialogView.findViewById(R.id.button18);
//                Button button19 = (Button) dialogView.findViewById(R.id.button19);
//                Button button20 = (Button) dialogView.findViewById(R.id.button20);
//                Button button21 = (Button) dialogView.findViewById(R.id.button21);
//                dialog.setView(dialogView);
//                dialog.show();
//
//                View.OnClickListener listener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Button?????????TextView,???TextView????????????View,????????????????????????
//                        Button btn = (Button) v;
//                        //Button??????????????????Id(?????????????????????)??????????????????????????????????????????
//                        switch (btn.getId()) {
//                            case R.id.button1:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button2:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button3:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button4:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button5:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button6:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button7:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button8:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button9:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button10:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button11:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button12:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button13:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button14:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button15:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button16:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button17:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button18:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button19:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button20:
//                                region_string = "?????????";
//                                break;
//                            case R.id.button21:
//                                region_string = "??????";
//                                break;
//                        }
//                        dialog.dismiss();
//
//                        TextRegion.setText("????????????????????????:" + region_string);
//
//                    }
//                }; //listener end
//
//                button1.setOnClickListener(listener);
//                button2.setOnClickListener(listener);
//                button3.setOnClickListener(listener);
//                button4.setOnClickListener(listener);
//                button5.setOnClickListener(listener);
//                button6.setOnClickListener(listener);
//                button7.setOnClickListener(listener);
//                button8.setOnClickListener(listener);
//                button9.setOnClickListener(listener);
//                button10.setOnClickListener(listener);
//                button11.setOnClickListener(listener);
//                button12.setOnClickListener(listener);
//                button13.setOnClickListener(listener);
//                button14.setOnClickListener(listener);
//                button15.setOnClickListener(listener);
//                button16.setOnClickListener(listener);
//                button17.setOnClickListener(listener);
//                button18.setOnClickListener(listener);
//                button19.setOnClickListener(listener);
//                button20.setOnClickListener(listener);
//                button21.setOnClickListener(listener);
//
//            }
//        }, 0);
//
//
//    }

    //??????????????????
    public void Release() {
        Intent intent = new Intent(getActivity(), Release.class);
        startActivity(intent);
    }

    //??????View
    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {


        class ViewHolder extends RecyclerView.ViewHolder {

            private final ImageView imageView1, imageView4;
            private final TextView text1, text2, text3, liketext;
            private final TextView mTextanimal, mTextgender, mTextphone, mTextvaccine, mTextligation;

            private final ViewPager2 pager2;
            private final CircleIndicator3 indicator3;


            //????????????ID ???????????????????????????  finID??????????????? super(itemView)????????? itemView
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

                mTextanimal = itemView.findViewById(R.id.mTextanimal);
                mTextgender = itemView.findViewById(R.id.mTextgender);
                mTextphone = itemView.findViewById(R.id.mTextphone);
                mTextvaccine = itemView.findViewById(R.id.mTextvaccine);
                mTextligation = itemView.findViewById(R.id.mTextligation);

            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyle_item, parent, false);
            return new ViewHolder(view);
        }

        @Override  //?????????????????????????????? ??????holder.?????? finID???????????????
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (MainActivity.count_AFrament1 == MainActivity.AFragment_sum + 1) {
                arrayList = new ArrayList<>();
                arrayList.add(MainActivity.AFragment1[position + 1]);
                arrayList.add(MainActivity.AFragment1_2[position + 1]);
                arrayList.add(MainActivity.AFragment1_3[position + 1]);

                viewPager2Adapter = new ViewPager2Adapter(arrayList);
                holder.pager2.setAdapter(viewPager2Adapter);
                holder.indicator3.setViewPager(holder.pager2);
            }


            holder.imageView1.setImageResource(R.drawable.ic_loading2);

            holder.text1.setText(arrayList_name.get(position));
            holder.text2.setText(arrayList_text.get(position));
            holder.text3.setText(arrayList_region.get(position));

            holder.liketext.setText(array_like.get(position).toString());

            holder.mTextanimal.setText("??????:"+arrayList_animal.get(position));
            holder.mTextgender.setText("??????:"+arrayList_gender.get(position));
            holder.mTextphone.setText("?????????Line:"+arrayList_phone.get(position));
            holder.mTextvaccine.setText("??????:"+arrayList_vaccine.get(position));
            holder.mTextligation.setText("??????:"+arrayList_ligation.get(position));



            //??????????????????????????????????????? ???????????????
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
                            //??????
                            holder.imageView1.setImageDrawable(resource);
                            linearLayout.removeAllViews();

                        }
                    });

            //????????????
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
                            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

        }

        @Override   //????????????
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



            if (position == 0) {

                holder.imageView.setImageResource(R.drawable.load_66);
                Glide.with(getActivity()).load(arrayList.get(position)).error(R.drawable.load_66).placeholder(R.drawable.ic_loading).into(holder.imageView);

            }
            if (position == 1) {

                holder.imageView.setImageResource(R.drawable.load_66);
                Glide.with(getActivity()).load(arrayList.get(position)).error(R.drawable.load_66).placeholder(R.drawable.ic_loading).into(holder.imageView);
            }
            if (position == 2) {

                holder.imageView.setImageResource(R.drawable.load_66);
                Glide.with(getActivity()).load(arrayList.get(position)).error(R.drawable.load_66).placeholder(R.drawable.ic_loading).into(holder.imageView);
            }


        }


        @Override
        public int getItemCount() {
            return 3;
        }


    }


}