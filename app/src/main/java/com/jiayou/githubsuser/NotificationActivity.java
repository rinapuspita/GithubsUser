package com.jiayou.githubsuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jiayou.githubsuser.receiver.GithubReceiver;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Switch mSwitch;
    TextView change;
    private GithubReceiver githubReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int notif = sharedPreferences.getInt("notif", 0);
        mSwitch = findViewById(R.id.switchNotification);
        change = findViewById(R.id.changeSet);
        if (notif == 1){
            mSwitch.setChecked(true);
        }else {
            mSwitch.setChecked(false);
        }
        onClickSwitch();
        githubReceiver = new GithubReceiver();
    }

    public void onClickSwitch(){
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notif", 1);
                    editor.commit();
                    githubReceiver.setNotif(getApplicationContext());
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notif", 0);
                    editor.commit();
                    githubReceiver.cancelAlarm(getApplicationContext());
                }
            }
        });
    }

    public void handleSet(View view) {
        Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(mIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return super.onOptionsItemSelected(item);
    }
}
