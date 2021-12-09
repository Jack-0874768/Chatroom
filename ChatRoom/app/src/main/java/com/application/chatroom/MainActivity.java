package com.application.chatroom;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> stringList = new ArrayList<>();
    private Button create_button;
    private ListView homeList;
    private DatabaseReference chat;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_button = findViewById(R.id.create_button);
        homeList = findViewById(R.id.homeList);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        homeAdapter = new HomeAdapter(MainActivity.this,new ArrayList<>());
        homeList.setAdapter(homeAdapter);
        chat = FirebaseDatabase.getInstance().getReference().child("chat");
        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringList.clear();
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    DataSnapshot next = iterator.next();
                    stringList.add(next.getKey());
                }
                homeAdapter.addAll(stringList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("chatKey",homeAdapter.getItem(i));
                startActivity(intent);
            }
        });
    }


}