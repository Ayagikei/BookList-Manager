package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
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
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        ConstraintLayout container;

        public ViewHolder(View view){
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.titleText);
            contentTextView = (TextView) view.findViewById(R.id.contentText);
            container = (ConstraintLayout) view.findViewById(R.id.itemLayout);
        }
    }

    public BookAdapter(List<Book> bookList,Context context){
        mContext = context;
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

        holder.container.setOnClickListener(view -> {
            TextView contentTextView = ((ViewGroup) view).findViewById(R.id.contentText);
            Intent intent = new Intent(view.getContext(), ScrollingActivity.class);
            intent.putExtra("book",book.getId());
            ActivityOptions option = ActivityOptions
                .makeSceneTransitionAnimation((Activity) view.getContext(), contentTextView, "share_text");
            view.getContext().startActivity(intent, option.toBundle());
        });

        holder.container.setOnLongClickListener(view ->{
            PopupMenu mPopupMenu = new PopupMenu(view.getContext(), view);
            mPopupMenu.getMenuInflater().inflate(R.menu.item_select_menu,mPopupMenu.getMenu());
            mPopupMenu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }




}
