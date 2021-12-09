package com.application.chatroom;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import io.github.rockerhieu.emojiconize.Emojiconize;


public class ChatActivity extends AppCompatActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    private ImageView ivFinish;
    private DatabaseReference reference;
    private ListView chatList;
    private Button send;
    private EmojiconEditText postEt;
    private FirebaseUser currentUser;
    private String uid;

    List<ChatEntity> chatActivities = new ArrayList<>();
    private String chatKey ;

    private ImageView chat_emoji;
    private View emojicons;
    private ChatAdapter chatAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Emojiconize.activity(this).go();
        super.onCreate(savedInstanceState);
        chatKey = getIntent().getStringExtra("chatKey");
        setContentView(R.layout.activity_chat);
        ivFinish = findViewById(R.id.iv_finish);
        chatList = findViewById(R.id.chat_list);
        title = findViewById(R.id.title);
        title.setText(chatKey);
        chat_emoji = findViewById(R.id.chat_emoji);
        emojicons = findViewById(R.id.emojicons);

        emojicons.setVisibility(View.GONE);
        chat_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojicons.getVisibility() ==View.GONE){
                    emojicons.setVisibility(View.VISIBLE);
                }else {
                    emojicons.setVisibility(View.GONE);
                }
            }
        });

        chatList.setDividerHeight(0);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatKey == null){
                    return;
                }
                if (TextUtils.isEmpty(postEt.getText().toString())){
                    return;
                }
                long l = System.currentTimeMillis();
                reference.child("chat").child(chatKey).child(l+"").child("content").setValue(postEt.getText().toString());
                reference.child("chat").child(chatKey).child(l+"").child("sendname").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
                reference.child("chat").child(chatKey).child(l+"").child("whosend").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
                reference.child("chat").child(chatKey).child(l+"").child("type").setValue("1");
                postEt.setText("");
            }
        });
        postEt = findViewById(R.id.post_et);
        uid = getIntent().getStringExtra("uid");
        chatAdapter = new ChatAdapter(this,chatActivities);
        chatList.setAdapter(chatAdapter);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        //Determine whether the current user has chat

        reference.child("chat").child(chatKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                chatActivities.clear();
                while (iterator.hasNext()){
                    DataSnapshot next = iterator.next();
                            if ( next.child("sendname").getValue()!=null
                                    &&next.child("content").getValue()!=null
                                    &&next.child("type").getValue()!=null
                                    &&next.child("whosend").getValue()!=null
                            ){
                                ChatEntity chatEntity = new ChatEntity();
                                chatEntity.setWhosend(next.child("whosend").getValue().toString());
                                chatEntity.setContent(next.child("content").getValue().toString());
                                chatEntity.setSendname(next.child("sendname").getValue().toString());
                                chatEntity.setType(next.child("type").getValue().toString());
                                chatActivities.add(chatEntity);
                            }



                }


                chatAdapter.addAll(chatActivities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {

    }



    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        postEt.setText(postEt.getText().toString()+emojicon.getEmoji());
        postEt.setSelection(postEt.getText().toString().length());
    }
}