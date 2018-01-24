package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class BookAddActivity extends AppCompatActivity {

    private String s_title = null;
    private String s_author = null;
    private String s_content = null;
    private Date date = null;
    private Book book = null;
    private TextView finishTimeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("增加/编辑书籍");

        //编辑的时候获取传递过来的book对象
        Intent intent = getIntent();

        int bookid = intent.getIntExtra("book",-1);
        finishTimeTextView = (TextView)findViewById(R.id.finishTimeTextView);

        if (bookid != -1) {
            List<Book> books = DataSupport.select("title","author","content","finishDate").where("id = ?",String.valueOf(bookid)).find(Book.class);
            book = books.get(0);
        }

        if(book != null ) {
            EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
            EditText bookAuthor = (EditText) findViewById(R.id.author);
            EditText bookContent = (EditText) findViewById(R.id.content);
            Button button = (Button)findViewById(R.id.book_add_or_edit_button) ;
            button.setText("编辑");
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookContent.setText(book.getContent());
            DateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
            if(book.getFinishDate()!=null)
            finishTimeTextView.setText(dateFormat.format(book.getFinishDate()));
            else finishTimeTextView.setText("阅读未完成");
        }else finishTimeTextView.setText("阅读未完成");





    }

    public void addBook(View view){

        boolean isNewBook = false;
        EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
        EditText bookAuthor = (EditText) findViewById(R.id.author);
        EditText bookContent = (EditText) findViewById(R.id.content);

        s_title = bookTitle.getText().toString();
        s_author = bookAuthor.getText().toString();
        s_content = bookContent.getText().toString();

        //新增or修改的差分
        if(book == null){
        book = new Book();
        isNewBook = true;
        }


        if(TextUtils.isEmpty(bookTitle.getText().toString().trim()))
        {
            Toast.makeText(getApplicationContext(), "书名不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        book.setTitle(s_title);
        book.setAuthor(s_author);
        book.setContent(s_content);
        book.setBookClass(0);
        if(date == null) {
            book.setToDefault("finishDate");
        }
        else {
            book.setFinishDate(date);
        }

        if(book.isSaved())      book.updateAll("id = ?",String.valueOf(book.getId()));
        else    book.save();

        if(isNewBook) {
            Toast.makeText(getApplicationContext(), "成功添加书籍",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else    {
            Toast.makeText(getApplicationContext(), "成功编辑书籍",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScrollingActivity.class);
            intent.putExtra("book",book.getId());
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(book != null) {
            Intent intent = new Intent(this, ScrollingActivity.class);
            intent.putExtra("book", book.getId());
            startActivity(intent);
        }
    }

    public void dataPick(View view){
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), null,
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
                            date = dateFormat1.parse(String.valueOf(year) +" 年 " + String.valueOf(monthOfYear) + " 月 " + String.valueOf(dayOfMonth) + " 日 ");
                            finishTimeTextView.setText(String.valueOf(year) +" 年 " + String.valueOf(monthOfYear) + " 月 " + String.valueOf(dayOfMonth) + " 日 ");
                        }catch(ParseException pe){

                        }
                    }
                });
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "未完成",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        date = null;
                        finishTimeTextView.setText("阅读未完成");
                    }
                });

        datePicker.show();    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
