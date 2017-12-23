package com.example.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
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
    private TextView contentText;
    private RecyclerView recyclerView;
    private int lastPosition = 0;
    private int lastOffset = 0;
    private int sortby = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        refreshList();


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
        refreshList();
        scrollToPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
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

    public void refreshList(int i){
        List<Book> booklist = null;
        if(i == 0) {
            booklist = DataSupport.select("title","author","content").order("id desc").find(Book.class);
        }
        else if(i == 1){
        booklist = DataSupport.select("title","author","content").order("title COLLATE LOCALIZED ASC").find(Book.class);
        }
        else if(i == 2){
            booklist = DataSupport.select("title","author","content").order("id asc").find(Book.class);
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

    public int getSortby(){
        return sortby;
    }

    public void setSortby(int i) {
        sortby = i;
        refreshList(i);
    }
}
