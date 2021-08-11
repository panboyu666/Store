package pan.bo.yu.petadoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;


public class Release extends AppCompatActivity {

    private Button b1,b3;
    private int PICK_CONT_REQUEST=1;
    private ImageView Image1;
    private Uri uri,uri_compression;
    private String data_list;
    private EditText editText;
    private TextView text4;

    Context context=this;
    DatabaseReference myRef2;
    StorageReference storageReference,pic_storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);


        b1 =findViewById(R.id.button);
        Image1 = findViewById(R.id.imageView8);
        editText=findViewById(R.id.editTextTextMultiLine);
        text4=findViewById(R.id.text4);


        //watcher 觀察文本
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                int x = 150-i;
                text4.setText("剩餘"+x+"個字");

            }
        };
        //觀察編輯文本
        editText.addTextChangedListener(watcher);
        //選擇手機內部圖片
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        createfirebase();

    }


    //2擇1按鈕
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_ninjas:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    //建立firebase母子資料路徑
    public void createfirebase(){

        //建立連接
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //建立第一個母資料夾
        DatabaseReference myRef = database.getReference("datatex_mom" );
        // 建立母資料夾的子資料夾
        myRef2 = myRef.child("data_kid01");


    }

    //b3按鈕發布
    public void release(View view){

        Toast.makeText(context, "上傳中.. 成功將自動跳轉", Toast.LENGTH_LONG).show();
        //連線到FirebaseStorage
        storageReference = FirebaseStorage.getInstance().getReference();

        //讀取資料總筆數跟上傳
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                //得到現在總量筆數再+1
                int id_sum= (int)snapshot.getChildrenCount()+1;
                //上傳
                myRef2.child(String.valueOf(id_sum)).setValue(new TextString(editText.getText().toString()));
                //下兩行是storage
                pic_storage = storageReference.child(id_sum+"."+data_list);   //參數是上傳檔案名稱
                pic_storage.putFile(uri_compression).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "發布成功", Toast.LENGTH_SHORT).show();
                        Intent intent_Main = new Intent(Release.this,MainActivity.class);
                        startActivity(intent_Main);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(context, "發布失敗", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        //以下IF 打開內部檔案後的動作
        if(requestCode==PICK_CONT_REQUEST){
            uri =data.getData();
            //下三行 讀取檔案的副檔名  傳給data_list
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
            data_list=mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

            Image1.setImageURI(uri);
            //得到uri的字串路徑
            String str_uri =""+uri;
           //壓縮機三方庫語法
            String filePath = SiliCompressor.with(this).compress(str_uri, this .getDir( "資料夾" , Context. MODE_PRIVATE));
            //壓縮後filepath轉uri
            uri_compression = Uri.parse(filePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    }
