package com.retrofit.using.java.data.remote;

import androidx.annotation.NonNull;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestUtils {

    public static final String TAG = RequestUtils.class.getSimpleName();

    /**
     * Create request body for string
     */
    @NonNull
    public static RequestBody createRequestBodyForString(String string) {
        return RequestBody.create(string, MediaType.parse("text/plain"));
    }

    /**
     * Create request body for file
     */
    public static RequestBody createRequestBodyForFile(File file) {
        return RequestBody.create(file, MediaType.parse("multipart/form-data"));
    }

    /**
     * Create multipart part request parameter
     */
    public static MultipartBody.Part createMultipartBody(String fileParameter, String filePath) {
        File file = new File(filePath);
        RequestBody requestBody = createRequestBodyForFile(file);
        return MultipartBody.Part.createFormData(fileParameter, file.getName(), requestBody);
    }
}