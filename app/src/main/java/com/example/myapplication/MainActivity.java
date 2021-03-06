package com.example.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "com.example.MyApplication.MESSAGE";
    public static final int SORTBY_ID_DESC = 0;
    public static final int SORTBY_FINISHTIME = 1;
    public static final int SORTBY_ABC = 2;
    public static final int SORTBY_ID = 3;
    public static final int SORTBY_FINISHTIME_DESC = 4;
    public static final int SORTBY_ABC_DESC = 5;


    public static final int CLASSBY_NORMAL = 0;
    public static final int CLASSBY_HIDE = 1;
    public static final int CLASSBY_FINISHED = 2;
    public static final int CLASSBY_UNFINISHED = 3;

    private TextView contentText;
    private RecyclerView recyclerView;
    private int lastPosition = 0;
    private int lastOffset = 0;
    private int sortby = SORTBY_ID_DESC;
    private int classby = CLASSBY_NORMAL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("书单");
        setSupportActionBar(toolbar);

        //获取持久化保存的排序方法
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        sortby = pref.getInt("sortby",SORTBY_ID);
        classby = pref.getInt("classby",CLASSBY_NORMAL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), BookAddActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshList(sortby,classby);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.getLayoutManager() != null) {
                    getPositionAndOffset();
                }
            }
        });

    }

    @Override
    public void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        refreshList(sortby,classby);
        scrollToPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(sortby,classby);
        scrollToPosition();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 点击Send按钮时的事件响应 Called when the user taps the Send button
     */
//    public void sendMessage(View view) {
//
//        EditText editText = (EditText) findViewById(R.id.editText2);
//
//        //实例化一个子View
//        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
//                R.layout.list_item_example, mContainerView, false);
//
//        ((TextView) newView.findViewById(android.R.id.text1)).setText(
//                editText.getText());
//
//
//        //设置删除按钮的监听
//        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContainerView.removeView(newView);
//
//                // If there are no rows remaining, show the empty view.
//                if (mContainerView.getChildCount() == 0) {
//                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
//        //添加子View
//        mContainerView.addView(newView, 0);
//        findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
//
//        // Do something in response to button
////        Intent intent = new Intent(this, DisplayMessageActivity.class);
////        EditText editText = (EditText) findViewById(R.id.editText2);
////        String message = editText.getText().toString();
////        intent.putExtra(EXTRA_MESSAGE, message);
////        startActivity(intent);
//    }


    public void openNewView(View view) {
//        textView2 = ((ViewGroup) view).findViewById(R.id.textView2);
//        Intent intent = new Intent(this, ScrollingActivity.class);
//        ActivityOptions option = ActivityOptions
//                .makeSceneTransitionAnimation(this, textView2, "share_text");
//        startActivity(intent, option.toBundle());
    }

    public void openAddBookView(View view) {
        Intent intent = new Intent(this, BookAddActivity.class);
        startActivity(intent);
    }

    public void refreshList(){
        //刷新书单
        List<Book> booklist = DataSupport.findAll(Book.class);
        Collections.reverse(booklist);
        BookAdapter bookAdapter = new BookAdapter(booklist,this);
        recyclerView.setAdapter(bookAdapter);
        if(bookAdapter.getItemCount() == 1)
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        else findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
    }


    public void refreshList(int i,int i_bookClass){
		//实现多种排序
        List<Book> booklist = null;

        String s_bookClass = null;

        if(i_bookClass==0){
            s_bookClass = "bookClass = 0";
        }
        else if(i_bookClass==1){
            s_bookClass = "bookClass = 1";
        }
        else if(i_bookClass==2){
            s_bookClass = "finishDate IS NOT NULL AND bookClass = 0";
        }else if(i_bookClass==3){
            s_bookClass = "finishDate IS NULL AND bookClass = 0";
        }

        if(sortby == SORTBY_ID_DESC) {
            booklist = DataSupport.select("title","author","content","finishDate").order("id DESC").where(s_bookClass).find(Book.class);
        }
        else if(sortby == SORTBY_FINISHTIME){
        booklist = DataSupport.select("title","author","content","finishDate").order("finishDate ASC").where(s_bookClass).find(Book.class);
        }
        else if(sortby == SORTBY_ABC){
            booklist = DataSupport.select("title","author","content","finishDate").order("title COLLATE LOCALIZED ASC").where(s_bookClass).find(Book.class);
        }
        else if(sortby == SORTBY_ID){
            booklist = DataSupport.select("title","author","content","finishDate").order("id ASC").where(s_bookClass).find(Book.class);
        }
        else if(sortby == SORTBY_FINISHTIME_DESC){
            booklist = DataSupport.select("title","author","content","finishDate").order("finishDate DESC").where(s_bookClass).find(Book.class);
        }
        else if(sortby == SORTBY_ABC_DESC){
            booklist = DataSupport.select("title","author","content","finishDate").order("title COLLATE LOCALIZED DESC").where(s_bookClass).find(Book.class);
		}

        BookAdapter bookAdapter = new BookAdapter(booklist,this);
        recyclerView.setAdapter(bookAdapter);
        if(bookAdapter.getItemCount() == 0)
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        else findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
    }

    /**
     * 记录RecyclerView当前位置
     */
    private void getPositionAndOffset() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if(topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    public void scrollToPosition() {
        if(recyclerView.getLayoutManager() != null && lastPosition >= 0) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }

    //获取当前的排序方法
    public int getSortby(){
        return sortby;
    }

    //设置新的排序方法
    public void setSortby(int sortby) {
        //持久化保存
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("sortby",sortby);
        editor.apply();

        this.sortby = sortby;
        refreshList(i,classby);
    }

    public int getClassby(){
        return classby;
    }

    public void setClassBy(int i) {
        //持久化保存
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("classby",i);
        editor.apply();

        classby = i;
        refreshList(sortby,i);
    }
}
