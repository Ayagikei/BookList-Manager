package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by hth on 2017/12/18 0018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<Book> mBookList;
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;
        TextView titleTextView;
        TextView contentTextView;
        ConstraintLayout container;

        public ViewHolder(View view){
            super(view);
            headerTextView = (TextView) view.findViewById(R.id.sortbyText);
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
        View view = null;
        ViewHolder holder = null;

        switch(viewType) {

            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_header, parent, false);
                holder = new ViewHolder(view);
                break;

            case 1:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_example, parent, false);
                holder = new ViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        switch (getItemViewType(position)) {

            //Header Item
            case 0:

                //获取已选择项
                int sortby =  ((MainActivity) mContext).getSortby();

                switch (sortby) {
                    case 0:holder.headerTextView.setText("按添加排序 ▼");
                        break;
                    case 1:holder.headerTextView.setText("按拼音顺序 ▼");
                        break;
                    case 2:holder.headerTextView.setText("按添加倒序排序 ▼");
                        break;
                    case 3:holder.headerTextView.setText("按完成时间排序 ▼");
                        break;
                }

                holder.headerTextView.setOnClickListener(view -> {
                    PopupMenu mPopupMenu = new PopupMenu(view.getContext(), view);
                    mPopupMenu.getMenuInflater().inflate(R.menu.item_sortby_menu, mPopupMenu.getMenu());



                    switch (sortby) {
                        case 0:mPopupMenu.getMenu().findItem(R.id.item_sortby_id).setChecked(true);
                            break;
                        case 1:mPopupMenu.getMenu().findItem(R.id.item_sortby_abc).setChecked(true);
                            break;
                        case 2:mPopupMenu.getMenu().findItem(R.id.item_sortby_id_desc).setChecked(true);
                            break;
                        case 3:mPopupMenu.getMenu().findItem(R.id.item_sortby_finishtime).setChecked(true);
                    }

                    mPopupMenu.setOnMenuItemClickListener(menuItem -> {
                        int title = menuItem.getItemId();

                        switch(title){
                            case (R.id.item_sortby_id):
                                ((MainActivity) mContext).setSortby(0);
                                break;

                            case (R.id.item_sortby_abc):
                                ((MainActivity) mContext).setSortby(1);
                                break;

                            case (R.id.item_sortby_id_desc):
                                ((MainActivity) mContext).setSortby(2);
                                break;
                            case (R.id.item_sortby_finishtime):
                                ((MainActivity) mContext).setSortby(3);
                                break;
                        }

                        return true;

                    });
                    mPopupMenu.show();

                });
                break;

            //Normal Item
            case 1:

                Book book = mBookList.get(position-1);
                holder.titleTextView.setText(book.getTitle());
                holder.contentTextView.setText(book.getContent());

                holder.container.setOnClickListener(view -> {
                    TextView contentTextView = ((ViewGroup) view).findViewById(R.id.contentText);
                    Intent intent = new Intent(view.getContext(), ScrollingActivity.class);
                    intent.putExtra("book", book.getId());
                    ActivityOptions option = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) view.getContext(), contentTextView, "share_text");
                    view.getContext().startActivity(intent, option.toBundle());
                });

                holder.container.setOnLongClickListener((View view) -> {
                    PopupMenu mPopupMenu = new PopupMenu(view.getContext(), view);
                    mPopupMenu.getMenuInflater().inflate(R.menu.item_select_menu, mPopupMenu.getMenu());
                    mPopupMenu.setOnMenuItemClickListener(menuItem -> {
                        int title = menuItem.getItemId();

                        if (title == R.id.delete_item) {


                            new AlertDialog.Builder(mContext).setTitle("删除")
                                    .setMessage("你确定要删除该书籍吗？")
                                    .setPositiveButton("确定", (dialog, which) -> {
                                        // 点击“确认”后的操作
                                        book.delete();
                                        ((MainActivity) mContext).refreshList();
                                        ((MainActivity) mContext).scrollToPosition();
                                        Toast.makeText(mContext, "成功删除书籍",
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("返回", (dialog, which) -> {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                    }).show();
                        } else if (title == R.id.edit_item) {
                            Intent intent = new Intent(view.getContext(), BookAddActivity.class);
                            intent.putExtra("book", book.getId());
                            view.getContext().startActivity(intent);
                        }

                        return true;
                    });
                    mPopupMenu.show();
                    return true;
                });

                break;
        } // End of switch
    }

    @Override
    public int getItemCount() {
        return mBookList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        else return TYPE_NORMAL;
    }
}
