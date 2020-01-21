package com.simarjot.bookwala.model.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.MessagingActivity;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;

public class AllChatsAdapter extends FirestoreRecyclerAdapter<Chat, AllChatsAdapter.ChatHolder> {
    private Context mContext;

    public AllChatsAdapter(@NonNull FirestoreRecyclerOptions<Chat> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
        String otherPerson = model.getParticipants().get(1);
        Log.d(Helper.TAG, model.getParticipants().toString());
        holder.participantName.setText(otherPerson);
        holder.itemView.setOnClickListener(v -> {
            Intent messagingIntent = new Intent(mContext, MessagingActivity.class);
            messagingIntent.putExtra(MessagingActivity.OTHER_USER_UID, otherPerson);
            mContext.startActivity(messagingIntent);
        });
        Log.d(Helper.TAG, "AllChats: " + model.getParticipants().get(1));
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_person_details, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.d(Helper.TAG, e.getMessage());
    }

    class ChatHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        RoundedImageView participantImage;

        ChatHolder(@NonNull View itemView) {
            super(itemView);
            participantName = itemView.findViewById(R.id.participant_name);
            participantImage = itemView.findViewById(R.id.participant_image);
        }
    }
}
