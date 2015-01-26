
package com.pacoworks.imprinter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private CharacterImprinter imprinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button characterButton = (Button)findViewById(R.id.load_char_btn);
        final Button effectButton = (Button)findViewById(R.id.load_dung_btn);
        final Button monsterButton = (Button)findViewById(R.id.load_mns_btn);
        imprinter = new CharacterImprinter(this);
        characterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    characterButton.setEnabled(false);
                    imprinter.printCharacters();
                    characterButton.setEnabled(true);
                } catch (Exception e) {
                    characterButton.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        effectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    effectButton.setEnabled(false);
                    imprinter.printEffects();
                    effectButton.setEnabled(true);
                } catch (Exception e) {
                    effectButton.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        monsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    monsterButton.setEnabled(false);
                    imprinter.printMonsters();
                    monsterButton.setEnabled(true);
                } catch (Exception e) {
                    monsterButton.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
