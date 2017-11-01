package com.example.ninadgawankar.capturephoto;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.example.ninadgawankar.capturephoto.PermissionsProvider.verifyCameraPermission;

/**
 * Created by Ninad Gawankar on 31/10/2017.
 */

public class SecondActivity extends AppCompatActivity {
    public static final String DEFAULT_IMAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/BCIL/Kiabza/Image/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String CAPTURED_IMAGE_TWO_NAME;
    private static String CAPTURED_IMAGE_ONE_NAME;
    private String SELECTION_TYPE = "Capture Images";
    public static final int REQUEST_CAPTURE = 1;
    ImageView imageDisplay, imageViewFirst, imageViewSecond;
    RelativeLayout imageViewFirstBG, imageViewSecondBG;
    View tappedView;
    int imageIndex = 0;
    String CAPTURE_TIME_IMAGE_ONE = "",
            CAPTURE_TIME_IMAGE_TWO = "";

    String pictureImagePathOne = "", pictureImagePathTwo = "";
    public static String CAPTURED_IMAGE_ONE;
    public static String CAPTURED_IMAGE_TWO;
    private Uri picUri;
    private Bitmap thePic;
    private String selectedImagePath;
    private static final int PIC_CROP = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        customiseActionBar();
        verifyCameraPermission(SecondActivity.this);
        initialiseViews();
        setListeners();
        getData();
    }

    private void setListeners() {
    }

    private void getData() {

    }

    private void initialiseViews() {
        imageDisplay = (ImageView) findViewById(R.id.imageDisplay);
        imageViewFirst = (ImageView) findViewById(R.id.imageViewFirst);
        imageViewSecond = (ImageView) findViewById(R.id.imageViewSecond);
        imageViewFirstBG = (RelativeLayout) findViewById(R.id.imageViewFirstBG);
        imageViewSecondBG = (RelativeLayout) findViewById(R.id.imageViewSecondBG);
        imageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        startActivityForResult(takePictureIntent, 1);
                    }

                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";

                    String title = "ERROR";
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle(title)
                            .setMessage(errorMessage)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.cancel();
                                }
                            });

                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (tappedView.getId() == R.id.imageDisplay) {
                if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
                    try {
                        ChoosePhoto choosePhoto = new ChoosePhoto(SecondActivity.this);
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        File camFile = null;
                        try {
                            camFile = createImageFile();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        FileOutputStream fo;
                        try {
                            fo = new FileOutputStream(camFile);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri photoURI = FileProvider.getUriForFile(SecondActivity.this,getPackageName() + ".provider", camFile);
                        Uri uri = choosePhoto.handleCameraResult(photoURI);
                        Intent cropIntent = choosePhoto.cropImage(photoURI, uri);
                        picUri = uri;
                        Log.d("picUri", uri.toString());
                        camFile.deleteOnExit();
                        File fdelete = new File(photoURI.getPath());
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                System.out.println("file Deleted :" + photoURI.getPath());
                            } else {
                                System.out.println("file not Deleted :" + photoURI.getPath());
                            }
                        }

                        startActivityForResult(cropIntent, PIC_CROP);
                    }catch (Exception e){
                        String errorMessage = "Whoops - Something Went Wrong While Capturing Images. Please Try Again!";

                        String title = "ERROR";
                    }

                }else if(requestCode == PIC_CROP && resultCode == RESULT_OK) {
                    try {
                        thePic = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        if (thePic != null) {
                            String localProfilePath = storeImage(thePic);
                            Bitmap bitmap1  = getBitmap(localProfilePath);
                                imageViewFirst.setImageBitmap(bitmap1);
                                imageViewSecond.setImageBitmap(bitmap1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        String errorMessage = "Whoops - Something Went Wrong While Croping Image. Please Try Again!";

                        String title = "ERROR";
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SecondActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap decodeSampledBitmapFromResource(String resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;

    }


    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        try {

            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
//            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile.getPath();
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        //RUNTIME PERMISSION Android M
        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    return null;
                }
            }
        }

        String mImageName ="";
        File mediaFile;
        mImageName = SystemController.getDateTimeProvider()+ "IMG"+ ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        if(mediaFile.exists()){
            mediaFile.delete();
        }
        return mediaFile;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "temp",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        if(image.exists()){
            image.delete();
        }
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap rotateImage(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //
    private void switchImage(ImageView imageView1, ImageView imageView2) {
        imageView2.setImageDrawable(imageView1.getDrawable());
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}

