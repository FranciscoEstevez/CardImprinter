
package com.pacoworks.imprinter;

import android.app.Activity;
import android.content.ClipData;
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

    private static final int LOAD_CODE = 753;

    private CharacterImprinter imprinter;

    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadButton = (Button)findViewById(R.id.load_all_btn);
        imprinter = new CharacterImprinter(this);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                    i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(i, LOAD_CODE);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ClipData clip = data.getClipData();
            if (clip != null) {
                for (int i = 0; i < clip.getItemCount(); i++) {
                    Uri uri = clip.getItemAt(i).getUri();
                    String path = uri.getPath();
                    String extension = path.substring(path.lastIndexOf(".") + 1);
                    if ("yml".equals(extension)) {
                        if (requestCode == LOAD_CODE) {
                            try {
                                loadButton.setEnabled(false);
                                imprinter.printAll(path);
                                loadButton.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                loadButton.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Exception " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong file, must have extension .yml",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
