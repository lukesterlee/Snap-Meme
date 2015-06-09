package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    // ELEMENTS
    ImageView cameraButton;
    ImageView galleryButton;
    ImageView popularMemesButton;

    // KEY VALUE PAIRS
    private String filePath = "";

    public static final String FILE_PATH_KEY = "file_path";

    // FLAGS
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_GALLERY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

    }

    private void setUpListeners(boolean isResumed) {
        if (!isResumed) {
            cameraButton.setOnClickListener(null);
            galleryButton.setOnClickListener(null);
            popularMemesButton.setOnClickListener(null);
        } else {
            // GALLERY BUTTON
            galleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchGallery();
                }
            });

            // CAMERA BUTTON
            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchCamera();
                }
            });

            // POPULAR MEMES BUTTON
            popularMemesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchPopularMemes();
                }
            });
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    private void launchGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
        }
    }

    private void launchPopularMemes() {
        Intent popularMemesIntent = new Intent(getApplicationContext(), PopularMemesActivity.class);
        startActivity(popularMemesIntent);
    }

    private void initializeViews() {
        // FINDING VIEWS
        galleryButton = (ImageView) findViewById(R.id.iv_gallery);
        cameraButton = (ImageView) findViewById(R.id.iv_camera);
        popularMemesButton = (ImageView) findViewById(R.id.iv_pop);
    }

    private void gotoSelectStyle() {
        Intent selectStyleIntent = new Intent(MainActivity.this, SelectStyleActivity.class);
        selectStyleIntent.putExtra(FILE_PATH_KEY, filePath);
        startActivity(selectStyleIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                filePath = selectedImage.toString();
                gotoSelectStyle();
            } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                filePath = selectedImage.toString();
                gotoSelectStyle();
            }
        } catch (Exception e) {
        }
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

