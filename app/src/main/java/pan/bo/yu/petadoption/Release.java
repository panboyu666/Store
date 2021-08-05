package pan.bo.yu.petadoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


public class Release extends AppCompatActivity {

    private Button b1;
    private int PICK_CONT_REQUEST=1;
    private ImageView Image1;
    private Uri uri;
    private String data_list;
    private EditText editText;
    private TextView text4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        b1 =findViewById(R.id.button);

        Image1 = findViewById(R.id.imageView8);

        editText=findViewById(R.id.editTextTextMultiLine);
        text4=findViewById(R.id.text4);


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


        editText.addTextChangedListener(watcher);

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



    }



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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_CONT_REQUEST){
            uri =data.getData();
            Image1.setImageURI(uri);
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
            data_list=mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    }
