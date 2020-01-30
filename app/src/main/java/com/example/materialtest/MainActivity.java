package com.example.materialtest;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //可以左滑
    private DrawerLayout drawerLayout;

    private Fruit[] fruits = {
            new Fruit("Apple", R.drawable.apple),
            new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange),
            new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear),
            new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple),
            new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry),
            new Fruit("Mango", R.drawable.mango)
    };

    private List<Fruit> fruitList = new ArrayList<>();
    //适配器
    private FruitAdapter adapter;
    //下拉刷新控件
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  //让导航栏按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); //设置图标 homeAsUp
        }
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "FAB clicked", Toast.LENGTH_SHORT).show();
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        initFruits();   //初始化
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);   //context, 列数
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        //下拉刷新功能
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary); //设置下拉刷新进度条的颜色
        //设置下拉刷新的监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            //当触发的时候就会回调onRefresh()方法
            public void onRefresh() {
                refreshFruits();
            }
        });
    }

    //刷新水果
    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //线程先沉睡两秒钟,因为本地刷新速度太快了看不出来
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //将线程切回主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //重新加载水果
                        initFruits();
                        //通知数据发生了变化
                        adapter.notifyDataSetChanged();
                        //表示刷新事件结束,并隐藏刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home: //homeAsUp按钮的id永远都是这个
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
}
