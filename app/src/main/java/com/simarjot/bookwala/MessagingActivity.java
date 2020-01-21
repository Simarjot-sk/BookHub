package com.simarjot.bookwala;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.rpc.Help;
import com.simarjot.bookwala.helpers.Helper;
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
        getChatId(otherPerson);
    }

    private void getChatId(String otherPerson) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatUid_1 = currentUser + "-" + otherPerson;
        String chatUid_2 = otherPerson + "-" + currentUser;

        getQuery(chatUid_1).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                addQueryToRecyclerView(chatUid_1);
            } else {
                Log.d(Helper.TAG, "chatUid_1 does not exist");
                addQueryToRecyclerView(chatUid_2);
            }
        });
    }

    private void addQueryToRecyclerView(String chatUid) {
        Query query = getQuery(chatUid).collection("messages").orderBy("createdAt", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mMessagesRecyclerView.setLayoutManager(layoutManager);
        mMessagesRecyclerView.setAdapter(new MessageAdapter(options));
    }

    private DocumentReference getQuery(String chatUid){
        return FirebaseFirestore.getInstance().collection("chats").document(chatUid);
    }
}
