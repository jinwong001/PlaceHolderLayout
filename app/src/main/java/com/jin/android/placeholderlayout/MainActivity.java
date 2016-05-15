package com.jin.android.placeholderlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jin.android.lib.PlaceHolderLayout;

public class MainActivity extends AppCompatActivity {
    PlaceHolderLayout mPlaceHolderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlaceHolderLayout=(PlaceHolderLayout)findViewById(R.id.activity_main);
        PlaceHolderLayout.PlaceholderEmpty.Builder emptyBuilder = new PlaceHolderLayout.PlaceholderEmpty.Builder();
        emptyBuilder.setTitle("空布局");
        mPlaceHolderLayout.setPlaceholderEmpty(emptyBuilder.build());

        PlaceHolderLayout.PlaceholderError.Builder errorBuilder = new PlaceHolderLayout.PlaceholderError.Builder();
        errorBuilder.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onRefreshStarted();
            }
        });
        mPlaceHolderLayout.setPlaceholderError(errorBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_empty:
                mPlaceHolderLayout.showEmpty();
                break;
            case R.id.menu_error:
                mPlaceHolderLayout.showError();
            case R.id.menu_content:
                mPlaceHolderLayout.showContent();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
