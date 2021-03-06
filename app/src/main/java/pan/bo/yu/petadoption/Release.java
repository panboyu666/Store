package pan.bo.yu.petadoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Release extends AppCompatActivity {

    private Button b1,b1_2,b1_3,b3,button15_1;
    private int PICK_CONT_REQUEST=1;
    private ImageView Image1;
    private ImageView Image2;
    private ImageView Image3;
    private Uri uri,uri_compression,uri_compression2,uri_compression3;
    private String data_list,region_string;
    private EditText editText;
    private TextView text4,regionText;

    private Bitmap headshot_bitmap;
    private String headshotUri;
    private String headshotName;

    private Context context=this;
    private DatabaseReference myRef2;
    private StorageReference storageReference,pic_storage,hradshot_storage;

    private int case_1=1;
    public static int xxx =0;

    //?????????????????????
    int x_sum=250;


    //???????????????????????????
    private String gender,ligation,vaccine;
    private String animal,phone;
    private Spinner mSpinner;
    private EditText meditTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        b1 =findViewById(R.id.button_1);
        b1_2 =findViewById(R.id.button_2);
        b1_3 =findViewById(R.id.button_3);
        b3 =findViewById(R.id.button3);
        button15_1=findViewById(R.id.button15);
        Image1 = findViewById(R.id.imageView8);
        Image2 = findViewById(R.id.imageView8_1);
        Image3 = findViewById(R.id.imageView8_2);
        editText=findViewById(R.id.editTextTextMultiLine);
        text4=findViewById(R.id.text4);


        regionText=findViewById(R.id.regionText);
        mSpinner=findViewById(R.id.spinner_animal);
        meditTextPhone=findViewById(R.id.editTextPhone);

        //watcher ????????????
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
                x_sum = 250-i;
                text4.setText("??????"+x_sum+"??????");
            }
        };
        //??????????????????
        editText.addTextChangedListener(watcher);

        //????????????????????????
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case_1=1;
                Intent intent =new Intent();
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });


        b1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case_1=2;
                Intent intent =new Intent();
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        b1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case_1=3;
                Intent intent =new Intent();
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });




        //firebase ???????????????storage??????????????????
        createfirebase();

        SharedPreferences preferences = getSharedPreferences("pet", 0);

        //????????????????????????uri_String
        headshotUri = preferences.getString("??????key","");

        //????????????????????????
        headshotName = preferences.getString("??????key","");

        //??????????????????
        button15_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                region();
            }
        });




    }


    //2???1??????
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.gender_1:
                gender="???";
                    break;
            case R.id.gender_2:
                gender="???";
                break;
            case R.id.gender_3:
                gender="?????????";
                    break;
            case R.id.ligation_1:
                ligation="?????????";
                break;
            case R.id.ligation_2:
                ligation="?????????";
                break;
            case R.id.ligation_3:
                ligation="?????????";
                break;
            case R.id.vaccine_1:
                vaccine="????????????";
                break;
            case R.id.vaccine_2:
                vaccine="???????????????";
                break;
            case R.id.vaccine_3:
                vaccine="?????????";
                break;

        }
    }

    //??????firebase??????????????????
    public void createfirebase(){
        //????????????
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //???????????????????????????
        DatabaseReference myRef = database.getReference("datatex_mom" );
        // ?????????????????????????????????
        myRef2 = myRef.child("data_kid01");
        //?????????FirebaseStorage
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    //b3????????????
    public void release(View view){



        if(uri_compression!=null
                && region_string!=null
                && x_sum<240
                && !headshotName.equals("??????")
                && gender!=null
                && ligation!=null
                && vaccine!=null){

            //get Spinner animal
            String[] s =getResources().getStringArray(R.array.animal);
            int i=mSpinner.getSelectedItemPosition();
            animal =s[i];
            //get phone
            phone= meditTextPhone.getText().toString();



            Toast.makeText(context, "?????????.. ?????????????????????", Toast.LENGTH_LONG).show();
            editText.setFocusable(false);
            //??????????????????????????????
            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    //???????????????????????????+1
                    int id_sum = (int) snapshot.getChildrenCount() + 1;
                    int like=0;

                    /**
                     * ??????????????? ???????????????,????????????,????????????,??????,??????,??????,??????,??????,??????
                     * **/
                    //?????? ??????
                    myRef2.child(String.valueOf(id_sum)).setValue(new TextString(
                            editText.getText().toString()
                            ,headshotName
                            ,region_string
                            ,like
                            ,animal
                            ,phone
                            ,gender
                            ,ligation
                            ,vaccine));

                    //??????hradshot
                    hradshot_storage=storageReference.child("headshot/"+"headshot_"+id_sum+".jpg");
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
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                Toast.makeText(Release.this, "??????????????????"+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();

                    xxx=0;

                    //??????pet_photo
                    pic_storage = storageReference.child("pet_photo/"+"pet_photo_"+id_sum+".jpg");   //???????????????????????????
                    pic_storage.putFile(uri_compression).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            xxx++;
                            if(xxx==3){
                                Intent intent_Main = new Intent(Release.this, MainActivity.class);
                                startActivity(intent_Main);
                            }
                        }
                    });

                    pic_storage = storageReference.child("pet_photo/"+"pet_photo_"+id_sum+"_2.jpg");   //???????????????????????????
                    pic_storage.putFile(uri_compression2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            xxx++;
                            if(xxx==3){
                                Intent intent_Main = new Intent(Release.this, MainActivity.class);
                                startActivity(intent_Main);
                            }
                        }
                    });

                    pic_storage = storageReference.child("pet_photo/"+"pet_photo_"+id_sum+"_3.jpg");   //???????????????????????????
                    pic_storage.putFile(uri_compression3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            xxx++;
                            if(xxx==3){
                                Intent intent_Main = new Intent(Release.this, MainActivity.class);
                                startActivity(intent_Main);
                            }
                        }
                    });

                }
                @Override
                public void onCancelled(@NonNull  DatabaseError error) {
                    Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(headshotName.equals("??????")){
            Toast.makeText(this, "?????????????????? ?????????GOOGLE?????????FB??????", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        //??????IF ??????????????????????????????
        if(requestCode==PICK_CONT_REQUEST){

            switch (case_1){
                case 1:
                    uri =data.getData();
                    Image1.setImageURI(uri);
                    b1.setText("?????????");
                    //????????? ????????????????????????  ??????data_list
                    ContentResolver contentResolver = getContentResolver();
                    MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
                    data_list=mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                    //??????uri???????????????
                    String str_uri =""+uri;
                    //????????????????????????
                    String filePath = SiliCompressor.with(this).compress(str_uri, this .getDir( "?????????" , Context. MODE_PRIVATE));
                    //?????????filepath???uri
                    uri_compression = Uri.parse(filePath);
                    break;
                case 2:
                    uri =data.getData();
                    Image2.setImageURI(uri);
                    b1_2.setText("?????????");
                    //????????? ????????????????????????  ??????data_list
                    ContentResolver contentResolver2 = getContentResolver();
                    MimeTypeMap mimeTypeMap2 =MimeTypeMap.getSingleton();
                    data_list=mimeTypeMap2.getExtensionFromMimeType(contentResolver2.getType(uri));
                    //??????uri???????????????
                    String str_uri2 =""+uri;
                    //????????????????????????
                    String filePath2 = SiliCompressor.with(this).compress(str_uri2, this .getDir( "?????????" , Context. MODE_PRIVATE));
                    //?????????filepath???uri
                    uri_compression2 = Uri.parse(filePath2);
                    break;
                case 3:
                    uri =data.getData();
                    Image3.setImageURI(uri);
                    b1_3.setText("?????????");
                    //????????? ????????????????????????  ??????data_list
                    ContentResolver contentResolver3 = getContentResolver();
                    MimeTypeMap mimeTypeMap3 =MimeTypeMap.getSingleton();
                    data_list=mimeTypeMap3.getExtensionFromMimeType(contentResolver3.getType(uri));
                    //??????uri???????????????
                    String str_uri3 =""+uri;
                    //????????????????????????
                    String filePath3 = SiliCompressor.with(this).compress(str_uri3, this .getDir( "?????????" , Context. MODE_PRIVATE));
                    //?????????filepath???uri
                    uri_compression3 = Uri.parse(filePath3);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //??????????????????
    public void region() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(Release.this);

                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(Release.this, R.layout.dialoglayout, null);

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
                        //Button?????????TextView,???TextView????????????View,????????????????????????
                        Button btn = (Button) v;
                        //Button??????????????????Id(?????????????????????)??????????????????????????????????????????
                        switch (btn.getId()) {
                            case R.id.button1:
                                region_string="?????????";
                                break;
                            case R.id.button2:
                                region_string="?????????";
                                break;
                            case R.id.button3:
                                region_string="?????????";
                                break;
                            case R.id.button4:
                                region_string="?????????";
                                break;
                            case R.id.button5:
                                region_string="?????????";
                                break;
                            case R.id.button6:
                                region_string="?????????";
                                break;
                            case R.id.button7:
                                region_string="?????????";
                                break;
                            case R.id.button8:
                                region_string="?????????";
                                break;
                            case R.id.button9:
                                region_string="?????????";
                                break;
                            case R.id.button10:
                                region_string="?????????";
                                break;
                            case R.id.button11:
                                region_string="?????????";
                                break;
                            case R.id.button12:
                                region_string="?????????";
                                break;
                            case R.id.button13:
                                region_string="?????????";
                                break;
                            case R.id.button14:
                                region_string="?????????";
                                break;
                            case R.id.button15:
                                region_string="?????????";
                                break;
                            case R.id.button16:
                                region_string="?????????";
                                break;
                            case R.id.button17:
                                region_string="?????????";
                                break;
                            case R.id.button18:
                                region_string="?????????";
                                break;
                            case R.id.button19:
                                region_string="?????????";
                                break;
                            case R.id.button20:
                                region_string="?????????";
                                break;
                            case R.id.button21:
                                region_string="??????";
                                break;
                        }
                        dialog.dismiss();
                        regionText.setText(region_string);
                        button15_1.setText("?????????"+region_string);
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


    }
