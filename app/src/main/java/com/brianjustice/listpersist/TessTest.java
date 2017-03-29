package com.brianjustice.listpersist;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TessTest extends AppCompatActivity {

    Bitmap image; //our image
    private TessBaseAPI mTess; //Tess API reference
    String datapath = ""; //path to folder containing language data file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tess_test);

        ImageView displayImage = (ImageView) findViewById(R.id.imageToOCR);

        //init image
        //image = BitmapFactory.decodeResource(getResources(), R.drawable.brianandamanda);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile( Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg");//, options );
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg");
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            bitmap = toGrayscale(bitmap);
            displayImage.setImageBitmap(bitmap);
            image= bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }


        datapath = getFilesDir()+ "/tesseract/";

        //make sure training data has been copied
        checkFile(new File(datapath + "tessdata/"));

        //init Tesseract API
        String language = "eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, language);


        Button ocrButton = (Button) findViewById(R.id.OCRbutton);
        ocrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mTess.setImage(image);
                String OCRresult = mTess.getUTF8Text();

                TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
                OCRTextView.setText(OCRresult);
            }
        });
    }

    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
