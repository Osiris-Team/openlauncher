package com.benny.openlauncher.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benny.openlauncher.R;
import com.benny.openlauncher.util.AppManager;
import com.benny.openlauncher.util.AppSettings;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

public class UsersActivity extends ColorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        LinearLayout ly = findViewById(R.id.users_list);
        AppSettings settings = AppSettings.get();
        ArrayList<AppSettings.User> users = settings.getUsers();
        for (int i = 0; i < users.size(); i++) {
            AppSettings.User user = users.get(i);
            Button btn = new Button(this);
            btn.setText("" + (i + 1) + ". User (" + user.handle.hashCode() + ")");
            btn.setBackgroundColor(user.color);
            btn.setOnClickListener(click -> {
                ColorPickerDialogBuilder.with(this)
                        .setOnColorSelectedListener(selectedColor -> {
                            btn.setBackgroundColor(selectedColor);
                            user.color = (int) selectedColor;
                            settings.setUsers(users); // Update color in saved data
                            // Apps UI gets refreshed once returning from settings
                            // thus no need to refresh icons here.
                        })
                        .build().show();
            });
            ly.addView(btn);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.pref_title__users));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance(this)._recreateAfterGettingApps = true;
        AppManager.getInstance(this).init();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
