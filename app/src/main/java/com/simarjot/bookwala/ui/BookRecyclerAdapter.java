package com.simarjot.bookwala.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.Category;

public class BookRecyclerAdapter extends FirestoreRecyclerAdapter<Book, BookRecyclerAdapter.BookHolder> {
    Context mContext;

    public BookRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.titleTV.setText(model.getTitle());
        if(model.getCategory() == Category.COLLEGE){
            holder.levelTV.setText("Semester " + model.getLevel());
        }else if(model.getCategory() == Category.SCHOOL){
            holder.levelTV.setText("Class " + model.getLevel());
        }else{
            holder.levelTV.setText("");
        }
        holder.priceTV.setText(model.getCurrency() + " " + model.getPrice());
        holder.itemView.setOnClickListener(v -> Toast.makeText(mContext, model.getTitle(), Toast.LENGTH_SHORT).show());
        Glide.with(mContext).load(model.getCoverDownloadUri()).into(holder.coverIV);
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        mContext = view.getContext();
        return new BookHolder(view);
    }

    public class BookHolder extends RecyclerView.ViewHolder{
        TextView priceTV;
        TextView titleTV;
        TextView levelTV;
        ImageView coverIV;
        BookHolder(@NonNull View itemView) {
            super(itemView);
            priceTV = itemView.findViewById(R.id.price_tv);
            titleTV = itemView.findViewById(R.id.title_tv);
            levelTV = itemView.findViewById(R.id.level_tv);
            coverIV = itemView.findViewById(R.id.cover_image);
        }
    }
}

