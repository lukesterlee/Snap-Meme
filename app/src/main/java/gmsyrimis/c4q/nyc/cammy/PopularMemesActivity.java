package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class PopularMemesActivity extends Activity {
    public static String IMAGE_URI_KEY = "uri";
    private ListView memeListView;
    private String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_memes);

        memeListView = (ListView) findViewById(R.id.meme_list_view);

        Integer[] memeResources = {
                R.drawable.gridone,
                R.drawable.gridtwo,
                R.drawable.gridthree,
                R.drawable.girdfour,
                R.drawable.gridfive,
                R.drawable.gridsix,
                R.drawable.gridseven,
                R.drawable.grideight,
                R.drawable.gridnine,
                R.drawable.gridten,
                R.drawable.grideleven,
                R.drawable.gridtwelve,
                R.drawable.gridthirteen,
                R.drawable.gridforteen,
                R.drawable.gridfifteen,
                R.drawable.gridsixteen,
        };
        MemeAdapter memeAdapter = new MemeAdapter(this, memeResources);
        memeListView.setAdapter(memeAdapter);

        memeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int meme = (Integer) parent.getItemAtPosition(position);
                Intent gotoPopMeme = new Intent(PopularMemesActivity.this, PopularMemesEditorActivity.class);
                imageUri += meme;
                gotoPopMeme.putExtra(IMAGE_URI_KEY, imageUri);
                startActivity(gotoPopMeme);
            }
        });

    }

    class MemeAdapter extends ArrayAdapter<Integer> {
        public MemeAdapter(Context context, Integer[] memeList) {
            super(context, 0, memeList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int singleMeme = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
            }
            ImageView appIcon = (ImageView) convertView.findViewById(R.id.meme_thumb);
            appIcon.setImageResource(singleMeme);
            return convertView;
        }
    }
}
