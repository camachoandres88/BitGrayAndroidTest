package co.bitgray.bitgraytest.utils;

import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
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

}
