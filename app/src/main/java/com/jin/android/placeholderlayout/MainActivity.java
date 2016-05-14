package com.jin.android.placeholderlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaceHolderLayout.PlaceholderEmpty.Builder emptyBuilder = new PlaceHolderLayout.PlaceholderEmpty.Builder();
        emptyBuilder.setTitle(getString(R.string.empty_title_apply));
        mPlaceHolderLayout.setPlaceholderEmpty(emptyBuilder.build());

        PlaceHolderLayout.PlaceholderError.Builder errorBuilder = new PlaceHolderLayout.PlaceholderError.Builder();
        errorBuilder.setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshStarted();
            }
        });
        mPlaceHolderLayout.setPlaceholderError(errorBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
