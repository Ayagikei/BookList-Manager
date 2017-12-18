package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BookAddActivity extends AppCompatActivity {

    private String s_title = null;
    private String s_author = null;
    private String s_content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);


    }

    public void addBook(View view){
        EditText bookTitle = (EditText) findViewById(R.id.bookTitle);
        EditText bookAuthor = (EditText) findViewById(R.id.author);
        EditText bookContent = (EditText) findViewById(R.id.content);

        s_title = bookTitle.getText().toString();
        s_author = bookAuthor.getText().toString();
        s_content = bookContent.getText().toString();

        Book book = new Book();
        book.setTitle(s_title);
        book.setAuthor(s_author);
        book.setContent(s_content);
        book.save();

        Toast.makeText(getApplicationContext(), "成功添加书籍",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
