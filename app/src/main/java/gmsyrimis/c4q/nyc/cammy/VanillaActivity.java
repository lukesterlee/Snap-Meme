package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VanillaActivity extends Activity {

    LinearLayout vanillaMemeLayout;
    Bitmap photoBitmap;

    private String filePath = "";

    private String topText;
    private String bottomText;

    public static final String FILE_NAME_PREFIX = "Meme_";
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss";
    public static final String FILE_NAME_SUFFIX = ".jpeg";

    public static final String FILE_PATH_KEY = "file_path";
    public static final String TOP_TEXT_KEY = "top";
    public static final String BOTTOM_TEXT_KEY = "bottom";


    EditText topRow;
    EditText bottomRow;

    int screenWidth;
    int screenHeight;

    private Button buttonShare;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla);

        initializeViews();

        restoreFromSavedInstanceState(savedInstanceState);

        setData();




    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            filePath = getIntent().getStringExtra(FILE_PATH_KEY);
            topText = "";
            bottomText = "";
        } else {
            filePath = savedInstanceState.getString(FILE_PATH_KEY);
            topText = savedInstanceState.getString(TOP_TEXT_KEY);
            bottomText = savedInstanceState.getString(BOTTOM_TEXT_KEY);
        }
    }

    private void setData() {
        topRow.setText(topText);
        bottomRow.setText(bottomText);

        try {
            photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        photoBitmap = Bitmap.createScaledBitmap(photoBitmap, screenWidth, screenHeight, true);
        vanillaMemeLayout.setBackground(new FakeBitmapDrawable(photoBitmap, 0));
    }

    private void setUpListener(boolean isResumed) {
        if (!isResumed) {
            buttonShare.setOnClickListener(null);
            buttonSave.setOnClickListener(null);
        } else {
            buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    process(vanillaMemeLayout);
                }
            });
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMeme();
                }
            });
        }
    }



    private void initializeViews() {
        vanillaMemeLayout = (LinearLayout) findViewById(R.id.vanilla_custom_image);
        topRow = (EditText) findViewById(R.id.vanilla_top_text);
        bottomRow = (EditText) findViewById(R.id.vanilla_bottom_text);
        buttonShare = (Button) findViewById(R.id.btShare);
        buttonSave = (Button) findViewById(R.id.button_save);
    }


    public void process(View view){

        Bitmap screenShotImage = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenShotImage);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(canvas);
        String imageFileName = new SimpleDateFormat(SIMPLE_DATE_FORMAT).format(new Date());
        String filename = FILE_NAME_PREFIX + imageFileName + FILE_NAME_SUFFIX;
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File outputFile = null;
        try {
            outputFile = File.createTempFile(filename, FILE_NAME_SUFFIX, picDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            screenShotImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //if(view.getId() == R.id.sendImage){
            Uri imageUri = Uri.fromFile(outputFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM,imageUri);
            intent.putExtra(Intent.EXTRA_TEXT,"Hey I have attached this picture");
            Intent chooser = Intent.createChooser(intent,"Send Picture");
            startActivity(chooser);
       // }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILE_PATH_KEY, filePath);
        outState.putString(TOP_TEXT_KEY, topText);
        outState.putString(BOTTOM_TEXT_KEY, bottomText);
    }

    public static Bitmap captureMeme(View memeView, int width, int height) {
        Bitmap screenShotImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenShotImage);
        memeView.layout(0, 0, memeView.getLayoutParams().width, memeView.getLayoutParams().height);
        memeView.draw(c);
        return screenShotImage;
    }

    public void saveMeme() {

        Bitmap screenShotImage = captureMeme(vanillaMemeLayout, screenWidth, screenHeight);

        String dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT).format(new Date());
        String filename = FILE_NAME_PREFIX + dateFormat + FILE_NAME_SUFFIX;

        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File outputFile = null;
        try {
            outputFile = File.createTempFile(filename, FILE_NAME_SUFFIX, picDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            screenShotImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri resultUri = Uri.fromFile(outputFile);
        addPictureToGallery(resultUri);

    }

    private void addPictureToGallery(Uri imageUri) {
        // this part is adding the completed meme picture to the gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListener(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListener(false);
    }
}
