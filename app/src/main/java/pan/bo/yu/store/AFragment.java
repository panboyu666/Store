package pan.bo.yu.store;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class AFragment extends Fragment {

    RecyclerView mRecyclerView;
    AFragment.MyListAdapter myListAdapter;
    ArrayList<String> arrayList = new ArrayList();
    ImageView  imageView2;
    ArrayList<ImageView> arrayList22 = new ArrayList<>();
    String[] sURL = new String[]{"https://images.goodsmile.info/cgm/images/product/20190925/8841/64026/large/83d0954b6bca2fdbdf736aa44387f4dd.jpg","https://i.pinimg.com/236x/10/99/0b/10990b52f7b7f8f02bbcd067ab9f1d8c.jpg"};

    //返回配置Xml文件View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.afragment, container, false );
        return view;

    }


    @Override //執行後內容 跟onCreate 差不多
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //finID前面要加view
        //textView =view.findViewById(R.id.text1);


        //下面是回收視圖碼
        arrayList22.add( imageView2 );

        mRecyclerView = view.findViewById( R.id.recycleview );
        mRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        //這行是分隔線
        mRecyclerView.addItemDecoration( new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL ) );

        arrayList.add( "p41" );
        arrayList.add( "p2" );

        myListAdapter = new MyListAdapter();
        mRecyclerView.setAdapter( myListAdapter );
        //上面那兩句也可寫成一句
        //mRecyclerView.setAdapter( new MyListAdapter());


        super.onViewCreated( view, savedInstanceState );


    }




    //回收View
     public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;
            private TextView textView,textView2 ;

            //這裡綁定ID 注意綁定語法不一樣  finID前面需要加 super(itemView)的內容 itemView
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView= itemView.findViewById(R.id.imageView);
                textView = itemView.findViewById(R.id.text1);
                textView2 = itemView.findViewById(R.id.text2);

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


            holder.textView.setText(arrayList.get(position));

            new DownloadImageTask(holder.imageView)
                    .execute(sURL[position]);


//            holder.tvId.setText(arrayList.get(position).get("Id"));
//            holder.tvSub1.setText(arrayList.get(position).get("Sub1"));
//            holder.tvSub2.setText(arrayList.get(position).get("Sub2"));
        }


        @Override   //顯示條列
        public int getItemCount() {
            return 2;
        }

    }
    //載入網路圖片
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}