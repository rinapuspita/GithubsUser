package com.jiayou.githubsuser;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiayou.githubsuser.adapter.FavoriteAdapter;
import com.jiayou.githubsuser.database.GithubHelper;
import com.jiayou.githubsuser.model.ItemResponse;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private GithubHelper githubHelper;
    private ArrayList<ItemResponse> itemResponse =  new ArrayList<>();
    private ItemResponse item;
    private FavoriteAdapter favoriteAdapter;
    private RecyclerView rvFavorit;
    ProgressBar progressBar;
    TextView erorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        progressBar = findViewById(R.id.progressBar);
        rvFavorit = findViewById(R.id.rv_favorit);
        erorText =  findViewById(R.id.notFavorit);
        favoriteAdapter = new FavoriteAdapter(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFavorit.setLayoutManager(layoutManager);
        rvFavorit.setAdapter(favoriteAdapter);

        githubHelper = new GithubHelper(getApplicationContext());
        githubHelper.open();
        itemResponse = githubHelper.getDataUser();
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Favorit");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (itemResponse.isEmpty()){
            erorText.setVisibility(View.VISIBLE);
            rvFavorit.setVisibility(View.GONE);
        } else{
            erorText.setVisibility(View.GONE);
            rvFavorit.setVisibility(View.VISIBLE);
        }
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((int) viewHolder.itemView.getTag());
            }
        };
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvFavorit);
    }

    private void removeItem(int id){
        itemResponse = githubHelper.getDataUser();
        githubHelper.userDelete(String.valueOf(id));
        favoriteAdapter.setItemResponse(itemResponse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemResponse = githubHelper.getDataUser();
        favoriteAdapter.setItemResponse(itemResponse);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        githubHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return super.onOptionsItemSelected(item);
    }
}
