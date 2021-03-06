package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private Book book;
    private int bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //获取传递过来的book对象
        Intent intent = getIntent();
//        book = (Book) intent.getSerializableExtra("book");
        bookid = intent.getIntExtra("book",-1);
        List<Book> books = DataSupport.select("title","author","content").where("id = ?",String.valueOf(bookid)).find(Book.class);
        book = books.get(0);
        TextView mContent = (TextView)findViewById(R.id.displayContent);
        mContent.setText(book.getContent());
        TextView mAuthor = (TextView)findViewById(R.id.authorText);
        mAuthor.setText(book.getAuthor());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(book.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookAddActivity.class);
                intent.putExtra("book",book.getId());
                view.getContext().startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);

        //刷新内容
        //获取传递过来的book对象
        Intent intent = getIntent();
//        book = (Book) intent.getSerializableExtra("book");
        bookid = intent.getIntExtra("book",-1);
        List<Book> books = DataSupport.select("title","author","content").where("id = ?",String.valueOf(bookid)).find(Book.class);
        book = books.get(0);
        TextView mContent = (TextView)findViewById(R.id.displayContent);
        mContent.setText(book.getContent());
        TextView mAuthor = (TextView)findViewById(R.id.authorText);
        mAuthor.setText(book.getAuthor());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(book.getTitle());
        this.setSupportActionBar(null);

    }


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
