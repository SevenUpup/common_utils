package com.fido.common.common_utils.blue_tooth;

/**
 * @author: FiDo
 * @date: 2025/3/26
 * @des:
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static String getPath(Context context, Uri uri) {
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            // 对于content:// URI，尝试获取实际路径
            String fileName = getFileName(context, uri);
            File cacheDir = context.getCacheDir();
            File tempFile = new File(cacheDir, fileName);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    copyInputStreamToFile(inputStream, tempFile);
                    return tempFile.getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // 对于file:// URI，直接返回路径
            return uri.getPath();
        }
        return null;
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
        }
    }
}
