package com.emilstrom.cosmic;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	public static final String TAG = "CosmicTag";
	public static MainActivity context;

	GLSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		context = this;

		surface = new GLSurface(this);

        setContentView(surface);
    }
}