package com.example.darek.lekcja11;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    private String CurrentPhoto;
    private String CurrentVideo;

    private Button photobutton;
    private Button videobutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        photobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePhoto();
                addGalleryPictrue();
            }
        });
        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takeVideo();
                addGalleryVideo();
            }
        });
    }
    private void initButtons() {
        photobutton = (Button) findViewById(R.id.photobutton);
        videobutton = (Button) findViewById(R.id.videobutton);
    }
    private void takePhoto() {
        Intent Photointent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Photointent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {}
            if (photoFile != null) {
                Uri URISavedImage = Uri.fromFile(photoFile);
                Photointent.putExtra(MediaStore.EXTRA_OUTPUT, URISavedImage);
                startActivityForResult(Photointent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }
    private void takeVideo() {
        Intent Videointent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (Videointent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {}
            if (videoFile != null) {
                Uri URISavedVideo = Uri.fromFile(videoFile);
                Videointent.putExtra(MediaStore.EXTRA_OUTPUT, URISavedVideo);
                startActivityForResult(Videointent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       /* String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);*/
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "myPhoto_" + timeStamp + ".jpg");

        CurrentPhoto = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File videosFolder = new File(Environment.getExternalStorageDirectory(), "MyVideos");
        videosFolder.mkdirs();
        File video = new File(videosFolder, "myVideo_" + timeStamp + ".mp4");

        CurrentVideo = video.getAbsolutePath();
        return video;
    }

    private void addGalleryPictrue() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(CurrentPhoto);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void addGalleryVideo() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(CurrentVideo);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
