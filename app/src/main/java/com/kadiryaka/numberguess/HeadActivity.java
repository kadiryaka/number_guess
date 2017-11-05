package com.kadiryaka.numberguess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by kadiryaka on 26.11.14.
 */
public class HeadActivity extends ActionBarActivity {

    private RadioGroup radiogroup;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //üst başlığı kaldırmak için
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head_activity);

        final Button startButton = (Button) findViewById(R.id.startButton);
        radiogroup = (RadioGroup) findViewById(R.id.valueRadioGroup);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadActivity.this, GameActivity.class);
                index = radiogroup.indexOfChild(findViewById(radiogroup.getCheckedRadioButtonId()));
                Bundle extras = new Bundle();
                extras.putString("index",String.valueOf(index));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
