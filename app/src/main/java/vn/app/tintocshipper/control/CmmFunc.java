package vn.app.tintocshipper.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.TypedValue;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.model.Order;

/**
 * Created by Admin on 7/25/2017.
 */

public class CmmFunc {
    //region parse Object to Json String
    public static String tryParseObject(Object object) {
        if (object == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            String json = gson.toJson(object, Object.class);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region parse string json to object
    @Nullable
    public static Object tryParseJson(String jsonString, Class<?> clazz) {
        Object obj = null;
        if (jsonString == null || jsonString.equals(""))
            return null;
        try {
            Gson gson = new Gson();
            obj = gson.fromJson(jsonString, clazz);
        } catch (Exception ex) {
        }
        return obj;
        //endregion
    }

    //region Format money
    public static String formatMoney(Object value) {
        String str = value + "";
        try {
            for (int i = str.length() - 3; i > 0; i -= 3) {
                str = new StringBuilder(str).insert(i, ",").toString();
            }
            return str;

        } catch (Exception e) {

        }
        return "";
    }

    //region Add Fragment
    public static void addFragment(FragmentActivity activity, int id, Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getName());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.add(id, fragment, fragment.getClass().getName());
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region parse string json to list
    public static List<?> tryParseList(String jsonString, Class<?> clazz) {
        List<?> retValue = null;
        if (TextUtils.isEmpty(jsonString))
            return null;
        JSONArray jArr;
        List<Object> objs = new ArrayList<>();
        try {
            jArr = new JSONArray(jsonString);
            for (int i = 0; i < jArr.length(); i++) {
                if (clazz.getName().contains("String")) {
                    objs.add(jArr.get(i));
                    continue;
                }
                JSONObject row = jArr.getJSONObject(i);
                Object obj = null;
                if (clazz.getName().contains("JSONObject")) {
                    obj = row;
                } else {
                    obj = CmmFunc.tryParseJson(row.toString(), clazz);
                }
                objs.add(obj);
            }
            retValue = objs;
        } catch (Exception e) {
        }
        return retValue;
    }
    //endregion

    //region replace fragment
    public static void replaceFragment(FragmentActivity activity, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(id, fragment, fragment.getClass().getName());
//        fragmentTransaction.replace(R.id.main_container, fragment, fragment.getClass().getName());
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
    //endregion

    //region replace main fragment
    public static void replaceMainFragment(FragmentActivity activity, int id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment, fragment.getClass().getName());
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
    //endregion

    public static int convertDpToPx(Activity activity, int dp) {
        try {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dp;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    //region Get bitmap từ hình ảnh trong thư mục, lưu vào file tạm, tránh trường hợp sai đường dẫn lúc upload ảnh
    public static File bitmapToFile(Activity activity, Bitmap bitmap) {
        File f = new File(activity.getCacheDir(), System.currentTimeMillis() + "");
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
    //endregion

    //region Scale hình ảnh về kích thước phù hợp
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    //endregion
    public static int dpToPixel(Activity activity, int dp) {
        Resources r = activity.getResources();
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        return px;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //region Get path from Uri
    public static String getPathFromUri(final Context context, final Uri uri) {

        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        }

                        // TODO handle non-primary volumes
                    }
                    // DownloadsProvider
                    else if (isDownloadsDocument(uri)) {

                        final String id = DocumentsContract.getDocumentId(uri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    }
                    // MediaProvider
                    else if (isMediaDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[]{
                                split[1]
                        };

                        return getDataColumn(context, contentUri, selection, selectionArgs);
                    }
                }
                // MediaStore (and general)
                else if ("content".equalsIgnoreCase(uri.getScheme())) {

                    // Return the remote address
                    if (isGooglePhotosUri(uri))
                        return uri.getLastPathSegment();

                    return getDataColumn(context, uri, null, null);
                }
                // File
                else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    //endregion

    //region Get data column
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    //endregion

    //region Persist image
    public static File persistImage(Context context, Bitmap bitmap) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, System.currentTimeMillis() + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {

        }
        return imageFile;
    }

    //endregion
}
