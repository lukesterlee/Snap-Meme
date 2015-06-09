package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class SelectStyleActivity extends Activity {

    ImageView demotivationalButton;
    ImageView vanillaButton;

    private String filePath = "";

    public static final String FILE_PATH_KEY = "file_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_style);

        initializeViews();

        restoreFromSavedInstanceState(savedInstanceState);

    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString(FILE_PATH_KEY) != null) {
                filePath = bundle.getString(FILE_PATH_KEY);
            }
        } else {
            filePath = savedInstanceState.getString(FILE_PATH_KEY);
        }
    }

    private void initializeViews() {
        demotivationalButton = (ImageView) findViewById(R.id.choose_demotivational);
        vanillaButton = (ImageView) findViewById(R.id.choose_vanilla);
    }

    private void setUpListeners(boolean isResumed) {
        if (!isResumed) {
            demotivationalButton.setOnClickListener(null);
            vanillaButton.setOnClickListener(null);
        } else {
            demotivationalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoDemotivational = new Intent(SelectStyleActivity.this, DemotivationalActivity.class);
                    gotoDemotivational.putExtra(FILE_PATH_KEY, filePath);
                    startActivity(gotoDemotivational);
                }
            });

            vanillaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoVanilla = new Intent(SelectStyleActivity.this, VanillaActivity.class);
                    gotoVanilla.putExtra(FILE_PATH_KEY, filePath);
                    startActivity(gotoVanilla);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILE_PATH_KEY, filePath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListeners(false);
    }
}
