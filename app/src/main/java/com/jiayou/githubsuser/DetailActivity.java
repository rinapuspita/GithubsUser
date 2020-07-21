package com.jiayou.githubsuser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.tabs.TabLayout;
import com.jiayou.githubsuser.database.DbContract;
import com.jiayou.githubsuser.database.DbHelper;
import com.jiayou.githubsuser.database.GithubHelper;
import com.jiayou.githubsuser.fragment.SectionsPagerAdapter;
import com.jiayou.githubsuser.model.DetailResponse;
import com.jiayou.githubsuser.model.ItemResponse;
import com.jiayou.githubsuser.services.ApiInterface;
import com.jiayou.githubsuser.services.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiayou.githubsuser.database.DbContract.UserColumns.TABLE_NAME;

public class DetailActivity extends AppCompatActivity {
    ImageView avatar;
    TextView tvNama , tvUsername, company, location, repository, numfollowers, numfollowing;
    ProgressBar progressBar;
    ItemResponse item;
    GithubHelper githubHelper;
    ArrayList<ItemResponse> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvNama = findViewById(R.id.tvName_detail);
        tvUsername = findViewById(R.id.tvUsername_detail);
        company = findViewById(R.id.tvCompany);
        location = findViewById(R.id.tvLocation);
        repository = findViewById(R.id.num_repo);
        numfollowers = findViewById(R.id.num_followers);
        numfollowing = findViewById(R.id.num_following);
        avatar = findViewById(R.id.avatar_detail);
        progressBar= findViewById(R.id.progressBar);
        TabLayout tabs = findViewById(R.id.tablayout_id);
        ViewPager viewPager = findViewById(R.id.view_pager);
        item = getIntent().getParcelableExtra("itemm");
        if(item != null){
            getList(item.getLogin());
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Detail " + item.getLogin());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setTag(item.getLogin());
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
        setOnClickFavoriteButton();
    }
    public void setImage(String image){
        Glide.with(DetailActivity.this)
                .asBitmap()
                .load(image)
                .into(avatar);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setOnClickFavoriteButton(){
        MaterialFavoriteButton materialFavoriteButton = findViewById(R.id.mfb_favorite);
        if (EXIST(item.getLogin())){
            materialFavoriteButton.setFavorite(true);
            materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if (favorite){
                        itemList = githubHelper.getDataUser();
                        githubHelper.userInsert(item);
                        Toast.makeText(DetailActivity.this, "Success added favorite", Toast.LENGTH_SHORT).show();
                    }else {
                        itemList = githubHelper.getDataUser();
                        githubHelper.userDelete(String.valueOf(item.getId()));
                        Toast.makeText(DetailActivity.this, "Success delete favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if (favorite){
                        itemList = githubHelper.getDataUser();
                        githubHelper.userInsert(item);
                        Toast.makeText(DetailActivity.this, "Success added favorite", Toast.LENGTH_SHORT).show();
                    }else {
                        itemList = githubHelper.getDataUser();
                        githubHelper.userDelete(String.valueOf(item.getId()));
                        Toast.makeText(DetailActivity.this, "Success delete favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean EXIST(String username){
        String change = DbContract.UserColumns.USERNAME + "=?";
        String[] changeArg = {username};
        String limit = "1";
        githubHelper = new GithubHelper(this);
        githubHelper.open();
        item = getIntent().getParcelableExtra("itemm");
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(TABLE_NAME, null, change, changeArg, null, null, null, limit);
        boolean exist = (cursor.getCount() > 0 );
        cursor.close();
        return exist;
    }

    private void getList(String username){
        progressBar.setVisibility(View.VISIBLE);
        String token = "03e6965398e6246b8501510daa726a42013516a4";
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Token "+token);
        Call<DetailResponse> call = service.getDetail(username);
        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("response api", response.body().toString());
                    String urlAvatar = response.body().getAvatarUrl();
                    String nama = response.body().getName();
                    String nama_user = response.body().getLogin();
                    String comp = response.body().getCompany();
                    String loc = response.body().getLocation();
                    Integer follower = response.body().getFollowers();
                    Integer following = response.body().getFollowing();
                    Integer repo = response.body().getPublicRepos();
                    tvNama.setText(nama);
                    tvUsername.setText(nama_user);
                    company.setText(comp);
                    location.setText(loc);
                    numfollowers.setText(String.valueOf(follower));
                    numfollowing.setText(String.valueOf(following));
                    repository.setText(String.valueOf(repo));
                    setImage(urlAvatar);
                } else {
                    Toast.makeText(DetailActivity.this, R.string.not_found_list, Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_favorite).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);

        if(item.getItemId() == R.id.action_favorite){
            Intent mIntent = new Intent(DetailActivity.this, FavoriteActivity.class);
            startActivity(mIntent);
        }if(item.getItemId() == R.id.action_notif_setting){
            Intent mIntent = new Intent(DetailActivity.this, NotificationActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
