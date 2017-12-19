package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class BookAddActivity extends AppCompatActivity {

    private String s_title = null;
    private String s_author = null;
    private String s_content = null;
    private Book book = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        //编辑的时候获取传递过来的book对象
        Intent intent = getIntent();


        int bookid = intent.getIntExtra("book",-1);

        if (bookid != -1) {
            List<Book> books = DataSupport.select("title","author","content").where("id = ?",String.valueOf(bookid)).find(Book.class);
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
        }


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

        book.setTitle(s_title);
        book.setAuthor(s_author);
        book.setContent(s_content);
        book.save();

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
        Intent intent = new Intent(this, ScrollingActivity.class);
        intent.putExtra("book",book.getId());
        startActivity(intent);
    }
}
