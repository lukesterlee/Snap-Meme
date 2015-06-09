package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DemotivationalActivity extends Activity {

    //ELEMENTS
    private ImageView pictureHolder;
    private Bitmap photoBitmap;
    private LinearLayout layoutHolder;
    private Button saveBTN;
    private Button shareBTN;
    private EditText topEditText;
    private EditText bottomEditText;

    // KEY VALUE PAIRS
    private String imageUri = "";
    public static String IMAGE_URI_KEY = "uri";
    private String topString;
    public static String TOP_TEXT_KEY = "top";
    private String bottomString;
    public static String BOTTOM_TEXT_KEY = "bottom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational);

        // FINDING VIEWS
        layoutHolder = (LinearLayout) findViewById(R.id.demotivational_holder);
        pictureHolder = (ImageView) findViewById(R.id.demotivational_custom_image);
        topEditText = (EditText) findViewById(R.id.demotivational_top_text);
        bottomEditText = (EditText) findViewById(R.id.demotivational_bottom_text);
        shareBTN = (Button) findViewById(R.id.share_btn);
        saveBTN = (Button) findViewById(R.id.save_btn);

        // LOADING
        if (savedInstanceState == null) {
            imageUri = getIntent().getExtras().getString(IMAGE_URI_KEY);
            topString = "";
            bottomString = "";
        } else {
            imageUri = savedInstanceState.getString(IMAGE_URI_KEY);
            topString = savedInstanceState.getString(TOP_TEXT_KEY);
            bottomString = savedInstanceState.getString(BOTTOM_TEXT_KEY);
        }

        // SETTING VALUES TO STRINGS AND IMAGEHOLDER
        topEditText.setText(topString);
        bottomEditText.setText(bottomString);
        try {
            photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoBitmap = Bitmap.createScaledBitmap(photoBitmap, 330, 210, true);
        pictureHolder.setImageDrawable(new FakeBitmapDrawable(photoBitmap, 0));

        // SAVE BUTTON
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri resultUri = makeViewBitmap(layoutHolder, layoutHolder.getWidth(), layoutHolder.getHeight());
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(resultUri);
                sendBroadcast(mediaScanIntent);
            }
        });
        
        // SHARE BUTTON
        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri resultUri = makeViewBitmap(layoutHolder, layoutHolder.getWidth(), layoutHolder.getHeight());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, resultUri);
                intent.putExtra(Intent.EXTRA_TEXT, "");
                Intent chooser = Intent.createChooser(intent, "Send Picture");
                startActivity(chooser);
            }
        });

    }


    public Uri makeViewBitmap(View view, int width, int height) {
        // VIEW TO BITMAP
        Bitmap viewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(viewBitmap);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(c);

        // FILE SETUP
        String uniqueIdentifier = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());
        String fileName = "Snapmeme" + uniqueIdentifier;
        File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // CREATING TEMP FILE
        File imageFILE = null;
        try {
            imageFILE = File.createTempFile(fileName, ".jpg", fileDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // STREAM INTO TEMP FILE
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFILE);
            viewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // URI TO BE PASSED
        return Uri.fromFile(imageFILE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_URI_KEY, imageUri);
        outState.putString(TOP_TEXT_KEY, topString);
        outState.putString(BOTTOM_TEXT_KEY, bottomString);
    }


}
