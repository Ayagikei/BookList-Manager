package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hth on 2017/12/18 0018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> mBookList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public ViewHolder(View view){
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.titleText);
            contentTextView = (TextView) view.findViewById(R.id.contentText);
        }
    }

    public BookAdapter(List<Book> bookList){
        mBookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_example,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.contentTextView.setText(book.getContent());
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }




}
