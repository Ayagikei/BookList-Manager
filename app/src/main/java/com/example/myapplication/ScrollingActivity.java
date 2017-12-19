package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        //获取传递过来的book对象
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        TextView mContent = (TextView)findViewById(R.id.displayContent);
        mContent.setText(book.getContent());
        TextView mAuthor = (TextView)findViewById(R.id.authorText);
        mAuthor.setText(book.getAuthor());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(book.getTitle());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookAddActivity.class);
                intent.putExtra("book",book);
                view.getContext().startActivity(intent);
            }
        });



    }
}
