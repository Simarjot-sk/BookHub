package com.simarjot.bookwala.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.simarjot.bookwala.R;
import com.simarjot.bookwala.SeeDetailsActivity;
import com.simarjot.bookwala.model.Book;

public class BookRecyclerAdapter extends FirestorePagingAdapter<Book, BookRecyclerAdapter.BookHolder> {
    Context mContext;

    public BookRecyclerAdapter(@NonNull FirestorePagingOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book book) {
        holder.titleTV.setText(book.getTitle());
        holder.subjectTV.setText(book.getSubject());
        holder.priceTV.setText(book.priceWithCurrency());
        holder.itemView.setOnClickListener(v -> {
            Intent seeDetailsIntent = new Intent(mContext, SeeDetailsActivity.class);
            seeDetailsIntent.putExtra(SeeDetailsActivity.BOOK_EXTRA, book.toJsonString());
            mContext.startActivity(seeDetailsIntent);
        });
        Glide.with(mContext).load(book.getCoverDownloadUri()).into(holder.coverIV);
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
        TextView subjectTV;
        ImageView coverIV;
        BookHolder(@NonNull View itemView) {
            super(itemView);
            priceTV = itemView.findViewById(R.id.price_tv);
            titleTV = itemView.findViewById(R.id.title_tv);
            subjectTV = itemView.findViewById(R.id.subject_tv);
            coverIV = itemView.findViewById(R.id.cover_image);
        }
    }
}

