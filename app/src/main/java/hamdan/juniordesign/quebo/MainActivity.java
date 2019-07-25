package hamdan.juniordesign.quebo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

import hamdan.juniordesign.quebo.photoedit.PhotoEditingActivity;

import static hamdan.juniordesign.quebo.utils.AppUtils.showToast;


/**
 * QUEBO is an android application based on google vision API and made up of two basic architectures-
 * Optical Character Recognition, helps to find and detect alphabets or words from an image.
 * The second one is POS Tagger that detects Parts of speech from a sentence or word.
 * QUEBO only gets alphabets during separation so if only “a” appears, it returns null because this
 * is no part of parts of speech.
 *
 * Our Team Members are
 *
 * Hamdan Kaiser
 * Mumin Az Zahira
 * Fatiha Jahan
 *
 * Special Thanks to
 * Mrs. Tanjila Farah
 * Lecturer, North South University
 *
 * Bugs to fix...
 * 1. Implementation of Articles
 * 2. Improvement of OCR Performance
 * 3. Read the least data from noisy pictures*/

public class MainActivity extends AppCompatActivity {

    public static Bitmap bitmap = null;
    public static boolean initiatePOS;
    String TAG = "MainActivity";
    EditText recognizeResult;

    ImageView showImage;
    Button openCamera, openGallery, runPOS,runOcr;

    public static boolean GetPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        showImage = findViewById(R.id.showImages);
        recognizeResult = findViewById(R.id.showResult);
        openCamera = findViewById(R.id.openCamera);
        openGallery = findViewById(R.id.openGallery);

        runOcr = findViewById(R.id.runOcr);

        runPOS = findViewById(R.id.showPOS);

        if (bitmap != null) {
            //display the image
            showImage.setImageBitmap(bitmap);
        }

        openGallery.setOnClickListener(v ->
                openGallery()
        );

        openCamera.setOnClickListener(v ->
                openCamera()
        );

       /* openPdf.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PdfActivity.class));

            //Future Work.....
        });*/



        runOcr.setOnClickListener(v -> {

            //performs OCR Operation....
            try {
                if (bitmap == null) {
                    //return toast if not found
                    showToast(MainActivity.this, "No text found for OCR!", false);
                    return;
                }

                recognizeResult.setText(null);

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                //TextRecognizer is a class of google vision

                //Finds and recognizes text in a supplied Frame.
                if (textRecognizer.isOperational()) {
                    //AI starts from here....
                    Log.d(TAG, "processImage: started");


                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {

                        //get each items and append items i in stringBuilder
                        TextBlock textBlock = items.valueAt(i);
                        stringBuilder.append(textBlock.getValue());
                        stringBuilder.append("\n");
                    }
                    recognizeResult.setText(stringBuilder.toString());

                } else {
                    Log.d(TAG, "processImage: not operational");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error init data OCR. Message: " + e.getMessage());
            }
        });


        runPOS.setOnClickListener(v -> {
            if (!initiatePOS) {
                showToast(MainActivity.this, "Initiating...", true);
                return;
            }

            String ocrText = recognizeResult.getText().toString().trim();
            if (ocrText.length() == 0) {
                showToast(MainActivity.this, "No text found for POS!", false);
                return;
            }
            startActivity(new Intent(MainActivity.this, ShowPOSActivity.class)
                    .putExtra("topic", ocrText));
        });

    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto, 2);
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    try {
                        InputStream stream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                        bitmap = BitmapFactory.decodeStream(stream);
                        if (stream != null) {
                            stream.close();
                        }
                        bitmap = bitmap.getWidth() > bitmap.getHeight()
                                ? rotateBitmap(bitmap, 90) : bitmap;
                        showImage.setImageResource(0);
                        showImage.setImageBitmap(bitmap);
                        saveImage(bitmap);

                    } catch (Exception e) {

                    }
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {

                    Uri contentURI = data.getData();
                    if (contentURI == null) break;
                    ContentResolver resolver = this.getContentResolver();
                    String typeOfMedia = Objects.requireNonNull(resolver.getType(contentURI)).toUpperCase().split("/")[0];

                    //if selected media is unsupported
                    if (typeOfMedia == null || !typeOfMedia.contains("IMAGE") && !typeOfMedia.contains("VIDEO"))
                        break;

                    //For Image
                    Log.e(TAG, "Got an Media! type of media ->" + typeOfMedia);

                    String[] projections = {MediaStore.Images.Media.DATA};
                    Cursor cursor = resolver.query(contentURI, null, null, null, null);
                    if (cursor == null) break;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projections[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Log.e(TAG, "File = " + filePath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        showImage.setImageResource(0);
                        //showImage.setImageBitmap(bitmap);
                        startActivity(new Intent(MainActivity.this, PhotoEditingActivity.class));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private void saveImage(Bitmap bitmap) {
        File direct = new File(Environment.getExternalStorageDirectory().toString() + "/Quebo/Images");
        if (!direct.exists()) {
            direct.mkdirs();
            Log.e("ProblemOnDirectory", "//////////////////////////////////");
        }

        FileOutputStream stream = null;
        try {
            File file = new File(direct,
                    "Quebo" + Calendar.getInstance().get(Calendar.YEAR)
                            + Calendar.getInstance().get(Calendar.MONTH)
                            + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                            + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg");

            stream = new FileOutputStream(file);
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                stream.write(bytes.toByteArray());
            }
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void checkPermission() {
        int permitAll = 1;
        String[] Permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!GetPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, permitAll);
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("1", "Granted");
                    } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("2", "Granted");
                    }
                }
            }
        }
    }
}
