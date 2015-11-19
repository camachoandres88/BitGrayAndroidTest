package co.bitgray.bitgraytest.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andrescamacho on 18/11/15.
 */
public class GeneralUtils {
    public static String dateToString(Date date) throws ParseException {
        SimpleDateFormat textFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatDate = textFormat.format(date);
        return formatDate;
    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat textFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        fecha = textFormat.parse(date);
        return fecha;
    }

    public static long dateToMilliseconds(Date date) throws ParseException {
        return date.getTime();
    }

    public static long dateToMillisecondsRemoveTime(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public  static Date millisecondsToDate(long milliseconds) throws ParseException {
        Date date = new Date();
        date.setTime(milliseconds);
        return date;
    }

    public static File createImageFile(String directory) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(directory);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        return image;
    }


    public static Bitmap getThumbnail(String path) throws Exception {

        final int THUMBNAIL_SIZE = 200;

        FileInputStream fis = new FileInputStream(path);
        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return imageBitmap;

    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
