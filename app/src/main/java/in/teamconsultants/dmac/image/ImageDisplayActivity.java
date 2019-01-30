package in.teamconsultants.dmac.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.utils.AppConstants;

public class ImageDisplayActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.image);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if(null == intent){
            finish();
        }

        String imgSrcType = intent.getStringExtra(AppConstants.INTENT_TAG.IMG_SRC_TYPE);

        if(imgSrcType.equals(AppConstants.IMAGE_SOURCE.URI)){
            String imageUri = intent.getStringExtra(AppConstants.INTENT_TAG.IMG_URI);
            Log.d(AppConstants.LOG_TAG, "imageUri: " + imageUri);
            Glide.with(this).load(imageUri).into(image).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
        }
        else {
            byte[] imgByteArr = intent.getByteArrayExtra(AppConstants.INTENT_TAG.IMG_BITMAP);
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
            image.setImageBitmap(imgBitmap);
        }

    }
}
