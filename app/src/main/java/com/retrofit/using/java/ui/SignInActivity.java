package com.retrofit.using.java.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.retrofit.using.java.R;
import com.retrofit.using.java.data.remote.ApiService;
import com.retrofit.using.java.data.remote.ApiServiceGenerator;
import com.retrofit.using.java.model.BaseResponse;
import com.retrofit.using.java.model.Data;
import com.retrofit.using.java.ui.base.BaseActivity;
import com.retrofit.using.java.utilities.ActivityUtils;
import com.retrofit.using.java.utilities.ValidationUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    public static final String TAG = SignInActivity.class.getSimpleName();

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private TextInputEditText emailTextInputEditText, passwordTextInputEditText;

    private MaterialButton appSignInMaterialButton, appSignUpLinkMaterialButton;

    private String emailString;
    private String passwordString;
    private String fcmTokenString          = "NJjMmJkNDAxCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZC5rb3RsaW4Kb25saW5lT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void initializeView() {
        emailTextInputLayout        = findView(R.id.emailTextInputLayout);
        emailTextInputEditText      = findView(R.id.emailTextInputEditText);
        passwordTextInputLayout     = findView(R.id.passwordTextInputLayout);
        passwordTextInputEditText   = findView(R.id.passwordTextInputEditText);

        appSignInMaterialButton     = findView(R.id.appSignInMaterialButton);
        appSignUpLinkMaterialButton = findView(R.id.appSignUpLinkMaterialButton);
    }

    @Override
    protected void initializeObject() {
    }

    @Override
    protected void initializeToolBar() {
    }

    @Override
    protected void initializeCallbackListener() {
    }

    @Override
    protected void addTextChangedListener() {
        emailTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    emailTextInputLayout.setErrorEnabled(true);
                    emailTextInputLayout.setError(getString(R.string.email_message_one));
                }
                else if(text.length() > 0)
                {
                    emailTextInputLayout.setError(null);
                    emailTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int emailValidCode = ValidationUtils.isValidEmail(emailTextInputEditText.getText().toString());
                if (emailValidCode > 0)
                {
                    if(emailValidCode == 1)
                    {
                        emailTextInputLayout.setError(getString(R.string.email_message_one));
                    }
                    else if(emailValidCode == 2)
                    {
                        emailTextInputLayout.setError(getString(R.string.email_message_two));
                    }
                }
            }
        });

        passwordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    passwordTextInputLayout.setErrorEnabled(true);
                    passwordTextInputLayout.setError(getString(R.string.password_message_one));
                }
                else if(text.length() > 0)
                {
                    passwordTextInputLayout.setError(null);
                    passwordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int passwordValidCode = ValidationUtils.isValidPassword(passwordTextInputEditText.getText().toString());
                if (passwordValidCode > 0)
                {
                    if(passwordValidCode == 1)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_one));
                    }
                    else if(passwordValidCode == 2)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_two));
                    }
                    else if(passwordValidCode == 3)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_three));
                    }
                    else if(passwordValidCode == 4)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_four));
                    }
                    else if(passwordValidCode == 5)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_five));
                    }
                    else if(passwordValidCode == 6)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_six));
                    }
                    else if(passwordValidCode == 7)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_seven));
                    }
                    else if(passwordValidCode == 8)
                    {
                        passwordTextInputLayout.setError(getString(R.string.password_message_eight));
                    }
                }
            }
        });
    }

    @Override
    protected void setOnClickListener() {
        appSignInMaterialButton.setOnClickListener(this);
        appSignUpLinkMaterialButton.setOnClickListener(this);
    }

    @Override
    protected void handleClickEvent(View view) {
        switch (view.getId()) {
            case R.id.appSignInMaterialButton:
                appSignIn();
                break;
            case R.id.appSignUpLinkMaterialButton:
                launchSignUpScreen();
                break;
            default:
                System.out.println("Invalid view id");
        }
    }

    public void appSignIn() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        emailString             = emailTextInputEditText.getText().toString();
        passwordString          = passwordTextInputEditText.getText().toString();

        if (validation(emailString, passwordString) == null)
        {
            ApiService apiService = ApiServiceGenerator.createService(SignInActivity.this, ApiService.class);

            Observable<Response<BaseResponse<Data>>> observable = apiService.signIn(emailString,passwordString, fcmTokenString);
            Observer<Response<BaseResponse<Data>>> observer = new Observer<Response<BaseResponse<Data>>>() {

                @Override
                public void onSubscribe(Disposable disposable) {
                    progressDialog.show();
                }

                @Override
                public void onNext(Response<BaseResponse<Data>> response) {
                    progressDialog.dismiss();
                    if (response != null) {
                        if (response.body() != null && response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        else
        {
            Toast.makeText(getApplicationContext(), validation(emailString, passwordString), Toast.LENGTH_SHORT).show();
        }
    }

    public void launchSignUpScreen() {
        ActivityUtils.launchActivity(SignInActivity.this, SignUpActivity.class);
    }

    public String validation(String email, String password) {
        int emailValidCode              = ValidationUtils.isValidEmail(email);
        int passwordValidCode           = ValidationUtils.isValidPassword(password);

        if(emailValidCode > 0)
        {
            if(emailValidCode == 1)
            {
                return getString(R.string.email_message_one);
            }
            else if(emailValidCode == 2)
            {
                return getString(R.string.email_message_two);
            }
        }
        else if(passwordValidCode > 0)
        {
            if(passwordValidCode == 1)
            {
                return getString(R.string.password_message_one);
            }
            else if(passwordValidCode == 2)
            {
                return getString(R.string.password_message_two);
            }
            else if(passwordValidCode == 3)
            {
                return getString(R.string.password_message_three);
            }
            else if(passwordValidCode == 4)
            {
                return getString(R.string.password_message_four);
            }
            else if(passwordValidCode == 5)
            {
                return getString(R.string.password_message_five);
            }
            else if(passwordValidCode == 6)
            {
                return getString(R.string.password_message_six);
            }
            else if(passwordValidCode == 7)
            {
                return getString(R.string.password_message_seven);
            }
            else if(passwordValidCode == 8)
            {
                return getString(R.string.password_message_eight);
            }
        }
        return null;
    }
}