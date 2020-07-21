package com.jiayou.githubsuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiayou.githubsuser.model.Item;
import com.jiayou.githubsuser.model.ItemResponse;
import com.jiayou.githubsuser.services.ApiInterface;
import com.jiayou.githubsuser.services.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static String api = "";
    String token = "03e6965398e6246b8501510daa726a42013516a4";
    private RecyclerView rvGithub;
    private ArrayList<ItemResponse> list = new ArrayList<>();
    TextView erorText;
    RecyclerView.Adapter mAdapter;
    SearchView searchView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        erorText =  findViewById(R.id.notFound);
        searchView = findViewById(R.id.search);
        progressBar = findViewById(R.id.progressBar);
        rvGithub = findViewById(R.id.rv_github);
        mAdapter = new GithubAdapter(list, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvGithub.setLayoutManager(layoutManager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getList(String user){
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.filling), Toast.LENGTH_SHORT).show();
        } else{
            cleanData();
            ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Token "+token);
            Call<Item> call = service.getUser(user);
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    if (response.isSuccessful()) {
                        list = response.body().getItems();
                        for (int i = 0; i < list.size(); i++){
                            GithubAdapter adapter = new GithubAdapter(list, getApplicationContext());
                            rvGithub.setAdapter(adapter);
                        }
                        rvGithub.setVisibility(View.VISIBLE);
                        if (list.isEmpty()) {
                            Toast.makeText(MainActivity.this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
                            erorText.setVisibility(View.VISIBLE);
                            rvGithub.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, R.string.not_found_list, Toast.LENGTH_SHORT).show();
                        erorText.setVisibility(View.VISIBLE);
                        rvGithub.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void cleanData(){
        list.clear();
        rvGithub.setVisibility(View.GONE);
        erorText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_favorite){
            Intent mIntent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(mIntent);
        }
        if(item.getItemId() == R.id.action_notif_setting){
            Intent mIntent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
