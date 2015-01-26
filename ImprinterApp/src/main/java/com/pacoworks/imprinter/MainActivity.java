
package com.pacoworks.imprinter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.nononsenseapps.filepicker.FilePickerActivity;

public class MainActivity extends ActionBarActivity {
    private static final int CHARACTER_CODE = 852;

    private static final int MONSTER_CODE = 456;

    private static final int EFFECT_CODE = 159;

    private CharacterImprinter imprinter;

    private Button characterButton;

    private Button effectButton;

    private Button monsterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        characterButton = (Button)findViewById(R.id.load_char_btn);
        effectButton = (Button)findViewById(R.id.load_dung_btn);
        monsterButton = (Button)findViewById(R.id.load_mns_btn);
        imprinter = new CharacterImprinter(this);
        characterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                    startActivityForResult(i, CHARACTER_CODE);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        effectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                    startActivityForResult(i, EFFECT_CODE);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        monsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                    startActivityForResult(i, MONSTER_CODE);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHARACTER_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            if (!"dom".equals(path.substring(path.lastIndexOf(".") + 1))){
                Toast.makeText(MainActivity.this, "Wrong file, must have extension .dom", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                characterButton.setEnabled(false);
                imprinter.printCharacters(path);
                characterButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                characterButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (requestCode == MONSTER_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            if (!"dom".equals(path.substring(path.lastIndexOf(".") + 1))){
                Toast.makeText(MainActivity.this, "Wrong file, must have extension .dom", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                monsterButton.setEnabled(false);
                imprinter.printMonsters(path);
                monsterButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                monsterButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (requestCode == EFFECT_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            if (!"dom".equals(path.substring(path.lastIndexOf(".") + 1))){
                Toast.makeText(MainActivity.this, "Wrong file, must have extension .dom", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                effectButton.setEnabled(false);
                imprinter.printEffects(path);
                effectButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                effectButton.setEnabled(true);
                Toast.makeText(MainActivity.this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
