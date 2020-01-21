package com.simarjot.bookwala.model.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.helpers.Helper;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> {

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        holder.message.setText(model.getMessageBody());
    }

    @Override
    public int getItemViewType(int position) {
        Message message = this.getItem(position);
        if (message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return Message.SENT;
        } else {
            return Message.RECIEVED;
        }
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Message.RECIEVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);
        }
        return new MessageHolder(view);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.d(Helper.TAG, e.getMessage());
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        private TextView message;

        MessageHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }
    }
}
