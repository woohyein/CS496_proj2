package com.example.cs496_proj2.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cs496_proj2.R;

public class AddContactActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    private EditText name;
    private EditText number;
    private ImageView image;
    private Uri uri = null;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        button = findViewById(R.id.certification);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        image = findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(name.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "이름을 추가하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(number.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "번호를 추가하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact contact;
                if(uri != null) contact = new Contact(phone(number.getText().toString()), name.getText().toString(), uri.toString());
                else contact = new Contact(number.getText().toString(), name.getText().toString(), null);
                GlobalContacts.getInstance().addContact(contact);

                finish();
            }
        });
    }

    public static String phone(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            image.setImageURI(uri);
        }
    }
}