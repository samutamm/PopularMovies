package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

public class SearchActivity extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("MOLO");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("NOTHONG SELECTED");
    }
}
