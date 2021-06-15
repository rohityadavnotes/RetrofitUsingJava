package com.retrofit.using.java.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.retrofit.using.java.R;
import com.retrofit.using.java.data.remote.ApiService;
import com.retrofit.using.java.data.remote.ApiServiceGenerator;
import com.retrofit.using.java.data.remote.RemoteConfiguration;
import com.retrofit.using.java.data.remote.glide.GlideCacheUtil;
import com.retrofit.using.java.data.remote.glide.GlideImageLoader;
import com.retrofit.using.java.data.remote.glide.GlideImageLoadingListener;
import com.retrofit.using.java.data.remote.picasso.PicassoImageLoader;
import com.retrofit.using.java.data.remote.picasso.PicassoImageLoadingListener;
import com.retrofit.using.java.ui.base.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RequestTestActivity extends BaseActivity {

    public static final String TAG = RequestTestActivity.class.getSimpleName();

    private ImageView userImageView;
    private ProgressBar imageLoadingProgressBar, progressBar;
    private TextView responseTextView;
    private MaterialButton requestMaterialButton, cancelRequestMaterialButton;

    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_request_test;
    }

    @Override
    protected void initializeView() {
        userImageView = findView(R.id.imageView);
        imageLoadingProgressBar = findView(R.id.imageLoadingProgressBar);
        progressBar = findView(R.id.requestProgressBar);
        responseTextView = findView(R.id.responseTextView);
        requestMaterialButton = findView(R.id.requestMaterialButton);
        cancelRequestMaterialButton = findView(R.id.cancelRequestMaterialButton);
    }

    @Override
    protected void initializeObject() {
    }

    @Override
    protected void initializeToolBar() {
    }

    @Override
    protected void initializeCallbackListener() {
       /* GlideCacheUtil.getInstance().clearAllCache(this);
        GlideImageLoader.load(
                this,
                "https://backend24.000webhostapp.com/Json/profile.jpg",
                R.drawable.user_placeholder,
                R.drawable.error_placeholder,
                userImageView,
                new GlideImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void imageLoadError() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                });*/

        PicassoImageLoader.load(
                this,
                "https://backend24.000webhostapp.com/Json/profile.jpg",
                R.drawable.user_placeholder,
                R.drawable.error_placeholder,
                userImageView,
                new PicassoImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void imageLoadError(Exception exception) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void addTextChangedListener() {
    }

    @Override
    protected void setOnClickListener() {
        requestMaterialButton.setOnClickListener(this);
        cancelRequestMaterialButton.setOnClickListener(this);
    }

    @Override
    protected void handleClickEvent(View view) {
        switch (view.getId()) {
            case R.id.requestMaterialButton:
                getSingleEmployee();
                //getPage();
                break;
            case R.id.cancelRequestMaterialButton:
                break;
            default:
                System.out.println("Invalid view id");
        }
    }

    private void getSingleEmployee() {
        ApiService apiService = ApiServiceGenerator.createService(RequestTestActivity.this, ApiService.class);

        Observable<Response<JsonObject>> observable = apiService.getEmployee();
        Observer<Response<JsonObject>> observer = new Observer<Response<JsonObject>>() {

            @Override
            public void onSubscribe(Disposable disposable) {
                progressBar.setVisibility(View.VISIBLE);
                subscribe = disposable;
            }

            @Override
            public void onNext(Response<JsonObject> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    if (response.body() != null && response.isSuccessful()) {
                        responseTextView.setText(response.body().toString());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                responseTextView.setText(e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getPage() {
        ApiService apiService = ApiServiceGenerator.createService(RequestTestActivity.this, ApiService.class);

        Observable<Response<JsonObject>> observable = apiService.getPage(RemoteConfiguration.API_KEY, "1");
        Observer<Response<JsonObject>> observer = new Observer<Response<JsonObject>>() {

            @Override
            public void onSubscribe(Disposable disposable) {
                progressBar.setVisibility(View.VISIBLE);
                subscribe = disposable;
            }

            @Override
            public void onNext(Response<JsonObject> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    if (response.body() != null && response.isSuccessful()) {
                        responseTextView.setText(response.body().toString());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
                responseTextView.setText(e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose(subscribe);
    }

    /**
     * unsubscribe
     *
     * @param disposable subscription information
     */
    public void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            Log.e(TAG, "Call dispose(Disposable disposable)");
        }
    }
}
