package com.simarjot.bookwala;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.simarjot.bookwala.model.chat.Chat;
import com.simarjot.bookwala.model.chat.Message;
import com.simarjot.bookwala.model.chat.MessageAdapter;

public class MessagingActivity extends AppCompatActivity {
    public static final String OTHER_USER_UID = "OTHER_USER_UID";
    private Chat mChat;
    //widgets
    private TextView mMessageET;
    private Button mSendBtn;
    private RecyclerView mMessagesRecyclerView;

    public MessagingActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        String currentPerson = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String otherPerson = getIntent().getStringExtra(OTHER_USER_UID);

        mMessageET = findViewById(R.id.message_et);
        mSendBtn = findViewById(R.id.send_btn);
        mMessagesRecyclerView = findViewById(R.id.message_rec);

        mChat = new Chat(currentPerson, otherPerson);

        mSendBtn.setOnClickListener(v -> {
            Toast.makeText(this, "sending..", Toast.LENGTH_SHORT).show();
            String msg = mMessageET.getText().toString();
            mMessageET.setText(null);
            Message message = new Message(currentPerson, msg);
            mChat.sendMessage(message, documentReference -> mMessagesRecyclerView.smoothScrollToPosition(mMessagesRecyclerView.getAdapter().getItemCount()));
        });

        Query query = mChat.query();
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mMessagesRecyclerView.setLayoutManager(layoutManager);
        mMessagesRecyclerView.setAdapter(new MessageAdapter(options));
    }
}
