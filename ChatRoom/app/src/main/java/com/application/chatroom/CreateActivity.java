package com.application.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class CreateActivity extends AppCompatActivity {
    private EditText create_name;
    private Button create_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ImageView ivFinish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        create_name = (EditText) findViewById(R.id.create_name);
        create_btn = (Button) findViewById(R.id.create_btn);
        ivFinish = (ImageView) findViewById(R.id.iv_finish);

        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(CreateActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount() {
        String email = create_name.getText().toString();

        reference.child("chat").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = snapshot.exists();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(CreateActivity.this, "Input name", Toast.LENGTH_SHORT).show();
                }else if(exists){
                    Toast.makeText(CreateActivity.this, " Name already exist", Toast.LENGTH_SHORT).show();
                }else {
                    long l = System.currentTimeMillis();
                    reference.child("chat").child(email).child(l+"").child("content").setValue("Welcome to my chat room!");
                    reference.child("chat").child(email).child(l+"").child("sendname").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
                    reference.child("chat").child(email).child(l+"").child("whosend").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
                    reference.child("chat").child(email).child(l+"").child("type").setValue("1");
                    Toast.makeText(CreateActivity.this, " Create suc!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateActivity.this,ChatActivity.class);
                    intent.putExtra("chatKey",email);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
