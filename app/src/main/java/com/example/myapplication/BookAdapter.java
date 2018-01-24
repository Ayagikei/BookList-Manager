package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by AyagiKei on 2017/12/18 0018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    public static final int SORTBY_ID_DESC = 0;
    public static final int SORTBY_FINISHTIME = 1;
    public static final int SORTBY_ABC = 2;
    public static final int SORTBY_ID = 3;
    public static final int SORTBY_FINISHTIME_DESC = 4;
    public static final int SORTBY_ABC_DESC = 5;

    private List<Book> mBookList;
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;
        TextView headerClassTextView;
        TextView titleTextView;
        TextView contentTextView;
        TextView tw_hasFinished;
        CheckBox cb_finish;
        ConstraintLayout container;

        public ViewHolder(View view){
            super(view);
            headerTextView = (TextView) view.findViewById(R.id.sortbyText);
            headerClassTextView= (TextView) view.findViewById(R.id.classbyText);
            titleTextView = (TextView) view.findViewById(R.id.titleText);
            contentTextView = (TextView) view.findViewById(R.id.contentText);
            tw_hasFinished = (TextView)view.findViewById(R.id.tw_hasFinished);
            cb_finish = (CheckBox)view.findViewById(R.id.cb_finish);
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

            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_header, parent, false);
                holder = new ViewHolder(view);
                break;

            case TYPE_NORMAL:
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
            case TYPE_HEADER:

                //获取已选择项
                int sortby =  ((MainActivity) mContext).getSortby();
                int classby =  ((MainActivity) mContext).getClassby();

                switch (sortby) {
                    case SORTBY_ID_DESC:holder.headerTextView.setText("按添加倒序 ▼");
                        break;
                    case SORTBY_FINISHTIME:holder.headerTextView.setText("按完成时间排序 ▼");
                        break;
                    case SORTBY_ABC:holder.headerTextView.setText("按拼音排序 ▼");
                        break;
                    case SORTBY_ID:holder.headerTextView.setText("按添加排序 ▼");
                        break;
                    case SORTBY_FINISHTIME_DESC:holder.headerTextView.setText("按完成时间倒序 ▼");
                        break;
                    case SORTBY_ABC_DESC:holder.headerTextView.setText("按拼音倒序 ▼");
                        break;
                }

                switch (classby) {
                    case 0:holder.headerClassTextView.setText("所有书籍 ▼");
                        break;
                    case 1:holder.headerClassTextView.setText("隐藏书籍 ▼");
                        break;
                    case 2:holder.headerClassTextView.setText("已完成书籍 ▼");
                        break;
                    case 3:holder.headerClassTextView.setText("未完成书籍 ▼");
                        break;
                }

                holder.headerTextView.setOnClickListener(view -> {
                    PopupMenu mPopupMenu = new PopupMenu(view.getContext(), view);
                    mPopupMenu.getMenuInflater().inflate(R.menu.item_sortby_menu, mPopupMenu.getMenu());



                    switch (sortby) {
                        case SORTBY_ID_DESC:mPopupMenu.getMenu().findItem(R.id.item_sortby_id).setChecked(true);
                            break;
                        case SORTBY_FINISHTIME:mPopupMenu.getMenu().findItem(R.id.item_sortby_finishtime).setChecked(true);
                            break;
                        case SORTBY_ABC:mPopupMenu.getMenu().findItem(R.id.item_sortby_abc).setChecked(true);
                            break;
                        case SORTBY_ID:mPopupMenu.getMenu().findItem(R.id.item_sortby_id).setChecked(true);
                            break;
                        case SORTBY_FINISHTIME_DESC:mPopupMenu.getMenu().findItem(R.id.item_sortby_finishtime).setChecked(true);
                            break;
                        case SORTBY_ABC_DESC:mPopupMenu.getMenu().findItem(R.id.item_sortby_abc).setChecked(true);
                            break;
                    }

                    mPopupMenu.setOnMenuItemClickListener(menuItem -> {
                        int title = menuItem.getItemId();

                        switch(title){
                            case (R.id.item_sortby_id):
                                if(((MainActivity) mContext).getSortby() == SORTBY_ID_DESC)  ((MainActivity) mContext).setSortby(SORTBY_ID);
                                    else ((MainActivity) mContext).setSortby(SORTBY_ID_DESC);
                                break;

                            case (R.id.item_sortby_finishtime):
                                if(((MainActivity) mContext).getSortby() == SORTBY_FINISHTIME)   ((MainActivity) mContext).setSortby(SORTBY_FINISHTIME_DESC);
                                else ((MainActivity) mContext).setSortby(SORTBY_FINISHTIME);
                                break;

                            case (R.id.item_sortby_abc):
                                if(((MainActivity) mContext).getSortby() == SORTBY_ABC)   ((MainActivity) mContext).setSortby(SORTBY_ABC_DESC);
                                else ((MainActivity) mContext).setSortby(SORTBY_ABC);
                                break;

                        }

                        return true;

                    });
                    mPopupMenu.show();

                 });

                holder.headerClassTextView.setOnClickListener(view -> {
                    PopupMenu mPopupMenu = new PopupMenu(view.getContext(), view);
                    mPopupMenu.getMenuInflater().inflate(R.menu.item_classby_menu, mPopupMenu.getMenu());



                    switch (classby) {
                        case 0:mPopupMenu.getMenu().findItem(R.id.item_classby_normal).setChecked(true);
                            break;
                        case 1:mPopupMenu.getMenu().findItem(R.id.item_classby_hide).setChecked(true);
                            break;
                        case 2:mPopupMenu.getMenu().findItem(R.id.item_classby_finished).setChecked(true);
                            break;
                        case 3:mPopupMenu.getMenu().findItem(R.id.item_classby_unfinished).setChecked(true);
                            break;
                    }

                    mPopupMenu.setOnMenuItemClickListener(menuItem -> {
                        int title = menuItem.getItemId();

                        switch(title){
                            case (R.id.item_classby_normal):
                                ((MainActivity) mContext).setClassBy(0);
                                break;

                            case (R.id.item_classby_hide):
                                ((MainActivity) mContext).setClassBy(1);
                                break;

                            case (R.id.item_classby_finished):
                                ((MainActivity) mContext).setClassBy(2);
                                break;

                            case (R.id.item_classby_unfinished):
                                ((MainActivity) mContext).setClassBy(3);
                                break;
                        }

                        return true;

                    });
                    mPopupMenu.show();

                });
                break;

            //Normal Item
            case TYPE_NORMAL:

                Book book = mBookList.get(position-1);
                holder.titleTextView.setText(book.getTitle());
                holder.contentTextView.setText(book.getContent());

                if(book.getFinishDate() != null) {
                    holder.cb_finish.setVisibility(View.GONE);
                    holder.tw_hasFinished.setVisibility(View.VISIBLE);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                    holder.tw_hasFinished.setText("已完成\n" +
                    dateFormat.format(book.getFinishDate()));
                }

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

                    if(book.getBookClass() == 0){
                        mPopupMenu.getMenu().findItem(R.id.cancel_hide_item).setVisible(false);
                    }else {
                        mPopupMenu.getMenu().findItem(R.id.hide_item).setVisible(false);
                    }

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
                        } else if (title == R.id.hide_item){
                            book.setBookClass(1);
                            book.save();
                            ((MainActivity) mContext).refreshList();
                            ((MainActivity) mContext).scrollToPosition();
                            Toast.makeText(mContext, "成功设为隐藏",
                                    Toast.LENGTH_SHORT).show();
                        } else if (title == R.id.cancel_hide_item){
                            book.setBookClass(0);
                            book.save();
                            ((MainActivity) mContext).refreshList();
                            ((MainActivity) mContext).scrollToPosition();
                            Toast.makeText(mContext, "成功取消隐藏",
                                    Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    });
                    mPopupMenu.show();
                    return true;
                });



                //列表中阅读完成CheckBox的逻辑
                holder.cb_finish.setOnCheckedChangeListener((compoundButton, b) -> {
                    Calendar calendar = Calendar.getInstance();
                    final DatePickerDialog datePicker = new DatePickerDialog(mContext, null,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePicker.setCancelable(true);
                    datePicker.setCanceledOnTouchOutside(true);
                    datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //确定的逻辑代码在监听中实现
                                    DatePicker picker = datePicker.getDatePicker();
                                    int year = picker.getYear();
                                    int monthOfYear = picker.getMonth()+1;
                                    int dayOfMonth = picker.getDayOfMonth();

                                    try{
                                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy 年 MM 月 dd 日 ");
                                        Date date = dateFormat1.parse(String.valueOf(year) +" 年 " + String.valueOf(monthOfYear) + " 月 " + String.valueOf(dayOfMonth) + " 日 ");
                                        book.setFinishDate(date);
                                        book.save();
                                        ((MainActivity) mContext).refreshList();
                                        ((MainActivity) mContext).scrollToPosition();
                                    }catch(ParseException pe){

                                    }
                                }
                            });
                    datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "未完成",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    book.setToDefault("finishDate");
                                    book.updateAll("id = ?",String.valueOf(book.getId()));

                                    ((MainActivity) mContext).refreshList();
                                    ((MainActivity) mContext).scrollToPosition();
                                }
                            });

                    datePicker.show();
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
