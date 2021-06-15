package com.retrofit.using.java.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.retrofit.using.java.R;
import com.retrofit.using.java.constants.AppConstants;
import com.retrofit.using.java.customimageview.CircleImageView;
import com.retrofit.using.java.data.remote.ApiService;
import com.retrofit.using.java.data.remote.ApiServiceGenerator;
import com.retrofit.using.java.data.remote.RequestUtils;
import com.retrofit.using.java.model.BaseResponse;
import com.retrofit.using.java.model.Data;
import com.retrofit.using.java.ui.base.BaseActivity;
import com.retrofit.using.java.utilities.ActivityUtils;
import com.retrofit.using.java.utilities.CameraUtils;
import com.retrofit.using.java.utilities.ImageAndVideoUtils;
import com.retrofit.using.java.utilities.ImplicitIntentUtils;
import com.retrofit.using.java.utilities.ValidationUtils;
import com.retrofit.using.java.utilities.compress.ConfigureCompression;
import com.retrofit.using.java.utilities.file.FileProviderUtils;
import com.retrofit.using.java.utilities.file.MediaFileUtils;
import com.retrofit.using.java.utilities.file.MemoryUtils;
import com.retrofit.using.java.utilities.file.RealPathUtils;
import com.retrofit.using.java.utilities.mediastore.MediaStoreUtils;
import com.retrofit.using.java.utilities.mediastore.MediaType;
import com.retrofit.using.java.utilities.mediastore.SourceOfMedia;
import com.retrofit.using.java.utilities.permissionutils.ManagePermission;
import com.retrofit.using.java.utilities.permissionutils.PermissionDialog;
import com.retrofit.using.java.utilities.permissionutils.PermissionName;
import com.retrofit.using.java.utilities.string.StringUtils;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    public static final String TAG = SignInActivity.class.getSimpleName();

    private CircleImageView profilePictureCircleImageView;
    private FloatingActionButton selectProfilePictureFloatingActionButton;

    private CountryCodePicker countryCodePicker;

    private TextInputLayout firstNameTextInputLayout, lastNameTextInputLayout, phoneNumberTextInputLayout, emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout;
    private TextInputEditText firstNameTextInputEditText, lastNameTextInputEditText, phoneNumberTextInputEditText, emailTextInputEditText, passwordTextInputEditText, confirmPasswordTextInputEditText;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private CheckBox termsConditionAndPrivacyPolicyCheckBox;
    private TextView termsConditionTextView;
    private TextView privacyPolicyTextView;

    private MaterialButton appSignUpMaterialButton;

    private TextView signInLinkTextView;

    private String pictureRealPathString;
    private String firstNameString;
    private String lastNameString;
    private String genderString;
    private String countryCodeString = "91";
    private String phoneNumberString;
    private String emailString;
    private String passwordString;
    private String confirmPasswordString;
    private String fcmTokenString;

    private boolean isTermsAndConditionsAccept = false;

    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 1001;
    private static final int MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE = 2001;
    private static final String[] MULTIPLE_PERMISSIONS =
            {
                    PermissionName.CAMERA,
                    PermissionName.READ_EXTERNAL_STORAGE,
            };

    private static final int SELECT_IMAGE_REQUEST_CODE = 3001;

    private ManagePermission managePermission;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initializeView() {
        profilePictureCircleImageView           = findView(R.id.profilePictureCircleImageView);
        selectProfilePictureFloatingActionButton= findView(R.id.selectProfilePictureFloatingActionButton);

        countryCodePicker                       = findView(R.id.countryCodePicker);

        firstNameTextInputLayout                = findView(R.id.firstNameTextInputLayout);
        firstNameTextInputEditText              = findView(R.id.firstNameTextInputEditText);
        lastNameTextInputLayout                 = findView(R.id.lastNameTextInputLayout);
        lastNameTextInputEditText               = findView(R.id.lastNameTextInputEditText);

        radioGroup                              = findView(R.id.genderRadioGroup);

        /* Clear RadioGroup, unchecked all the RadioButton */
        radioGroup.clearCheck();

        /* Add the Listener to the RadioGroup */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*
                 * The flow will come here when
                 * any of the radio buttons in the radioGroup
                 * has been clicked
                 */
                radioButton = (RadioButton)group.findViewById(checkedId);
            }
        });

        phoneNumberTextInputLayout              = findView(R.id.phoneNumberTextInputLayout);
        phoneNumberTextInputEditText            = findView(R.id.phoneNumberTextInputEditText);
        emailTextInputLayout                    = findView(R.id.emailTextInputLayout);
        emailTextInputEditText                  = findView(R.id.emailTextInputEditText);
        passwordTextInputLayout                 = findView(R.id.passwordTextInputLayout);
        passwordTextInputEditText               = findView(R.id.passwordTextInputEditText);
        confirmPasswordTextInputLayout          = findView(R.id.confirmPasswordTextInputLayout);
        confirmPasswordTextInputEditText        = findView(R.id.confirmPasswordTextInputEditText);

        termsConditionAndPrivacyPolicyCheckBox  = findView(R.id.termsConditionAndPrivacyPolicyCheckBox);
        termsConditionTextView                  = findView(R.id.termsConditionTextView);
        privacyPolicyTextView                   = findView(R.id.privacyPolicyTextView);

        appSignUpMaterialButton                 = findView(R.id.appSignUpMaterialButton);

        signInLinkTextView                      = findView(R.id.signInLinkTextView);
    }

    @Override
    protected void initializeObject() {
        managePermission = new ManagePermission(SignUpActivity.this);
    }

    @Override
    protected void initializeToolBar() {
    }

    @Override
    protected void initializeCallbackListener() {
    }

    @Override
    protected void addTextChangedListener() {
        firstNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    firstNameTextInputLayout.setErrorEnabled(true);
                    firstNameTextInputLayout.setError(getString(R.string.first_name_message_one));
                }
                else if(text.length() > 0)
                {
                    firstNameTextInputLayout.setError(null);
                    firstNameTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    lastNameTextInputLayout.setErrorEnabled(true);
                    lastNameTextInputLayout.setError(getString(R.string.last_name_message_one));
                }
                else if(text.length() > 0)
                {
                    lastNameTextInputLayout.setError(null);
                    lastNameTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countryCodeString = selectedCountry.getPhoneCode();
            }
        });

        phoneNumberTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    phoneNumberTextInputLayout.setErrorEnabled(true);
                    phoneNumberTextInputLayout.setError(getString(R.string.phone_number_message_one));
                }
                else if(text.length() > 0)
                {
                    phoneNumberTextInputLayout.setError(null);
                    phoneNumberTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int phoneValidCode = ValidationUtils.isPhoneNumberValid(countryCodeString, Objects.requireNonNull(phoneNumberTextInputEditText.getText()).toString());
                if (phoneValidCode > 0)
                {
                    if(phoneValidCode == 1)
                    {
                        phoneNumberTextInputLayout.setError(getString(R.string.phone_number_message_one));
                    }
                    else if(phoneValidCode == 2)
                    {
                        phoneNumberTextInputLayout.setError(getString(R.string.phone_number_message_two));
                    }
                    else if(phoneValidCode == 3)
                    {
                        phoneNumberTextInputLayout.setError(getString(R.string.phone_number_message_three));
                    }
                }
            }
        });

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
                int emailValidCode = ValidationUtils.isValidEmail(Objects.requireNonNull(emailTextInputEditText.getText()).toString());
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
                int passwordValidCode = ValidationUtils.isValidPassword(Objects.requireNonNull(passwordTextInputEditText.getText()).toString());
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

        confirmPasswordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    confirmPasswordTextInputLayout.setErrorEnabled(true);
                    confirmPasswordTextInputLayout.setError(getString(R.string.confirm_password_message_one));
                }
                else if(text.length() > 0)
                {
                    confirmPasswordTextInputLayout.setError(null);
                    confirmPasswordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int confirmPasswordValidCode = ValidationUtils.isValidConfirmPassword(passwordTextInputEditText.getText().toString().trim(), confirmPasswordTextInputEditText.getText().toString().trim());
                if (confirmPasswordValidCode > 0)
                {
                    if(confirmPasswordValidCode == 1)
                    {
                        confirmPasswordTextInputLayout.setError(getString(R.string.password_message_one));
                    }
                    else if(confirmPasswordValidCode == 2)
                    {
                        confirmPasswordTextInputLayout.setError(getString(R.string.confirm_password_message_one));
                    }
                    else if(confirmPasswordValidCode == 3)
                    {
                        confirmPasswordTextInputLayout.setError(getString(R.string.confirm_password_message_two));
                    }
                    else if(confirmPasswordValidCode == 4)
                    {
                        confirmPasswordTextInputLayout.setError(getString(R.string.confirm_password_message_three));
                    }
                }
            }
        });
    }

    @Override
    protected void setOnClickListener() {
        selectProfilePictureFloatingActionButton.setOnClickListener(this);
        termsConditionAndPrivacyPolicyCheckBox.setOnClickListener(this);
        termsConditionTextView.setOnClickListener(this);
        privacyPolicyTextView.setOnClickListener(this);
        appSignUpMaterialButton.setOnClickListener(this);
        signInLinkTextView.setOnClickListener(this);
    }

    @Override
    protected void handleClickEvent(View view) {
        switch (view.getId()) {
            case R.id.selectProfilePictureFloatingActionButton:
                if (CameraUtils.isDeviceSupportCamera(SignUpActivity.this))
                {
                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        if (managePermission.hasPermission(MULTIPLE_PERMISSIONS))
                        {
                            /* Is Granted, Do next code */
                            showPictureDialog(this);
                        }
                        else
                        {
                            /* If not granted, Request for Permission */
                            ActivityCompat.requestPermissions(SignUpActivity.this, MULTIPLE_PERMISSIONS, MULTIPLE_PERMISSION_REQUEST_CODE);
                        }
                    }
                    else
                    {
                        /* Already Granted, Do next code */
                        showPictureDialog(this);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.termsConditionAndPrivacyPolicyCheckBox:
                isTermsAndConditionsAccept = ((CheckBox) view).isChecked();
                break;
            case R.id.termsConditionTextView:
                break;
            case R.id.privacyPolicyTextView:
                break;
            case R.id.appSignUpMaterialButton:
                signUp();
                break;
            case R.id.signInLinkTextView:
                launchSignInScreen();
                break;
            default:
                System.out.println("Invalid view id");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            String permission = permissions[i];

                            if (permission.equalsIgnoreCase(PermissionName.CAMERA))
                            {
                                boolean showRationale = managePermission.shouldShowRequestPermissionRationale(permission);
                                if (showRationale)
                                {
                                    Log.e(TAG, "camera permission denied");

                                    ActivityCompat.requestPermissions(
                                            SignUpActivity.this,
                                            MULTIPLE_PERMISSIONS,
                                            MULTIPLE_PERMISSION_REQUEST_CODE);
                                    return;
                                }
                                else
                                {
                                    Log.e(TAG, "camera permission denied and don't ask for it again");

                                    PermissionDialog.permissionDeniedWithNeverAskAgain(
                                            SignUpActivity.this,
                                            R.drawable.permission_ic_camera,
                                            "Camera Permission",
                                            "Kindly allow Camera Permission from Settings, without this permission the app is unable to provide photo capture feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                            permission,
                                            MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                                    return;
                                }
                            }

                            if (permission.equalsIgnoreCase(PermissionName.READ_EXTERNAL_STORAGE))
                            {
                                boolean showRationale = managePermission.shouldShowRequestPermissionRationale(permission);
                                if (showRationale)
                                {
                                    Log.e(TAG, "read external storage permission denied");

                                    ActivityCompat.requestPermissions(SignUpActivity.this, MULTIPLE_PERMISSIONS, MULTIPLE_PERMISSION_REQUEST_CODE);
                                    return;
                                }
                                else
                                {
                                    Log.e(TAG, "read external storage permission denied and don't ask for it again");

                                    PermissionDialog.permissionDeniedWithNeverAskAgain(
                                            SignUpActivity.this,
                                            R.drawable.permission_ic_storage,
                                            "Read Storage Permission",
                                            "Kindly allow Read Storage Permission from Settings, without this permission the app is unable to provide file read feature. Please turn on permissions at [Setting] -> [Permissions]>",
                                            permission,
                                            MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                                    return;
                                }
                            }
                        }
                    }
                    Log.e(TAG, "all permission granted, do the task");
                    createdFileUriAndCaptureImage();
                }
                else
                {
                    Log.e(TAG, "Unknown Error");
                }
                break;
            default:
                throw new RuntimeException("unhandled permissions request code: " + requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MULTIPLE_PERMISSIONS_FROM_SETTING_REQUEST_CODE)
        {
            if (managePermission.hasPermission(MULTIPLE_PERMISSIONS))
            {
                Log.e(TAG, "permission granted from settings");
                createdFileUriAndCaptureImage();
            }
            else
            {
                Log.e(TAG, "permission is not granted, request for permission, from settings");
                ActivityCompat.requestPermissions(SignUpActivity.this, MULTIPLE_PERMISSIONS, MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        }

        if (requestCode == ImageAndVideoUtils.CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                String realPath = RealPathUtils.getRealPath(this, imageUri);

                File oldFile = new File(realPath);
                System.out.println("=========================OLD===========================" + MemoryUtils.getReadableFileSize(oldFile.length()));

                /* this file is store in a File externalFilesDir = context.getExternalFilesDir("Compress"); directory */
                File newFile = ConfigureCompression.getInstance(this).compressToFile(oldFile);
                System.out.println("=========================NEW===========================" + MemoryUtils.getReadableFileSize(newFile.length()));

                pictureRealPathString = newFile.getAbsolutePath();

                Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                if (bitmap != null)
                {
                    profilePictureCircleImageView.setImageBitmap(bitmap);
                }
            }
            else if (resultCode == RESULT_CANCELED)
            {
                if (android.os.Build.VERSION.SDK_INT >= 29)
                {
                    ContentResolver contentResolver = getApplicationContext().getContentResolver();
                    ContentValues updateContentValue = new ContentValues();
                    updateContentValue.put(MediaStore.Images.Media.IS_PENDING, true);
                    contentResolver.update(imageUri, updateContentValue, null, null);
                }

                Toast.makeText(getApplicationContext(), "User cancelled capture image", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SELECT_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                if (data != null)
                {
                    /* Here read store permission require */
                    imageUri = data.getData();

                    if (imageUri != null)
                    {
                        String realPath = RealPathUtils.getRealPath(this, imageUri);

                        File oldFile = new File(realPath);
                        System.out.println("=========================OLD===========================" + MemoryUtils.getReadableFileSize(oldFile.length()));

                        /* this file is store in a File externalFilesDir = context.getExternalFilesDir("Compress"); directory */
                        File newFile = ConfigureCompression.getInstance(this).compressToFile(oldFile);
                        System.out.println("=========================NEW===========================" + MemoryUtils.getReadableFileSize(newFile.length()));

                        pictureRealPathString = newFile.getAbsolutePath();

                        Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                        if (bitmap != null)
                        {
                            profilePictureCircleImageView.setImageBitmap(bitmap);
                        }
                    }
                }
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "User cancelled select image", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPictureDialog(Activity activity) {
        AlertDialog.Builder option = new AlertDialog.Builder(activity);
        option.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Capture photo from camera",
                "Select photo from gallery",
                "Cancel"
        };

        option.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                createdFileUriAndCaptureImage();
                                break;
                            case 1:
                                ImplicitIntentUtils.actionPickIntent(1, SignUpActivity.this, SELECT_IMAGE_REQUEST_CODE);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        option.show();
    }

    private void createdFileUriAndCaptureImage() {
        String customDirectoryName = "AppName";
        String fileName = MediaFileUtils.getRandomFileName(1);
        String extension = ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            Uri sourceUri = MediaStoreUtils.getSourceOfMedia(SourceOfMedia.EXTERNAL, MediaType.IMAGES);

            /* when only select image from gallery use this way */
            //imageUri = MediaStoreUtils.createImagesMediaFile(getApplicationContext(), sourceUri, fileName, fileName+extension, "image/jpeg", Environment.DIRECTORY_PICTURES, customDirectoryName);;

            /* else below*/
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, fileName);
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName + extension);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            long millis = System.currentTimeMillis();
            contentValues.put(MediaStore.Images.Media.DATE_ADDED, millis / 1000L);
            contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, millis / 1000L);
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, millis);
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + customDirectoryName);
            contentValues.put(MediaStore.Images.Media.IS_PENDING, false);
            imageUri = contentResolver.insert(sourceUri, contentValues);
        }
        else
        {
            File mediaFile = null;
            try {
                mediaFile = MediaFileUtils.createFile(SignUpActivity.this, 1, customDirectoryName, extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = FileProviderUtils.getFileUri(getApplicationContext(), mediaFile, AppConstants.PACKAGE_NAME);
        }

        ImageAndVideoUtils.cameraIntent(1, imageUri, SignUpActivity.this);
    }

    public void signUp() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        firstNameString         = firstNameTextInputEditText.getText().toString();
        lastNameString          = lastNameTextInputEditText.getText().toString();

        /*
         * Get the Radio Button which is set
         * If no Radio Button is set, -1 will be returned
         */
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1)
        {
            genderString            = "";
        }
        else
        {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
            genderString            = radioButton.getText().toString();
        }

        phoneNumberString       = phoneNumberTextInputEditText.getText().toString();
        emailString             = emailTextInputEditText.getText().toString();
        passwordString          = passwordTextInputEditText.getText().toString();
        confirmPasswordString   = confirmPasswordTextInputEditText.getText().toString();
        fcmTokenString          = "NJjMmJkNDAxCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZC5rb3RsaW4Kb25saW5lT";

        if (validation(pictureRealPathString,firstNameString, lastNameString, genderString, countryCodeString, phoneNumberString, emailString, passwordString, confirmPasswordString, isTermsAndConditionsAccept, fcmTokenString) == null)
        {
            ApiService apiService = ApiServiceGenerator.createService(SignUpActivity.this, ApiService.class);

            MultipartBody.Part profilePic       = RequestUtils.createMultipartBody("profilePic", pictureRealPathString);
            RequestBody firstNameRequestBody    = RequestUtils.createRequestBodyForString(firstNameString);
            RequestBody lastNameRequestBody     = RequestUtils.createRequestBodyForString(lastNameString);
            RequestBody genderRequestBody       = RequestUtils.createRequestBodyForString(genderString);
            RequestBody countryCodeRequestBody  = RequestUtils.createRequestBodyForString(countryCodeString);
            RequestBody phoneNumberRequestBody  = RequestUtils.createRequestBodyForString(phoneNumberString);
            RequestBody emailRequestBody        = RequestUtils.createRequestBodyForString(emailString);
            RequestBody passwordRequestBody     = RequestUtils.createRequestBodyForString(passwordString);
            RequestBody fcmTokenRequestBody     = RequestUtils.createRequestBodyForString(fcmTokenString);

            Observable<Response<BaseResponse<Data>>> observable = apiService.signUp(profilePic, firstNameRequestBody, lastNameRequestBody, genderRequestBody, countryCodeRequestBody, phoneNumberRequestBody, emailRequestBody, passwordRequestBody, fcmTokenRequestBody);
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
            Toast.makeText(getApplicationContext(), validation(pictureRealPathString,firstNameString, lastNameString, genderString, countryCodeString, phoneNumberString, emailString, passwordString, confirmPasswordString, isTermsAndConditionsAccept, fcmTokenString), Toast.LENGTH_SHORT).show();
        }
    }

    public String validation(String pictureRealPath, String firstName, String lastName, String gender, String countryCode, String phoneNumber, String email, String password, String confirmPassword, boolean isTermsAndConditionsAccept, String fcmToken) {
        int phoneValidCode              = ValidationUtils.isPhoneNumberValid(countryCode, phoneNumber);
        int emailValidCode              = ValidationUtils.isValidEmail(email);
        int passwordValidCode           = ValidationUtils.isValidPassword(password);
        int confirmPasswordValidCode    = ValidationUtils.isValidConfirmPassword(password, confirmPassword);

        if(StringUtils.isBlank(pictureRealPath))
        {
            return getString(R.string.picture_message_one);
        }
        else if(StringUtils.isBlank(firstName))
        {
            return getString(R.string.first_name_message_one);
        }
        else if(StringUtils.isBlank(lastName))
        {
            return getString(R.string.last_name_message_one);
        }
        else if(StringUtils.isBlank(gender))
        {
            return getString(R.string.gender_message_one);
        }
        else if(phoneValidCode > 0)
        {
            if(phoneValidCode == 1)
            {
                return getString(R.string.phone_number_message_one);
            }
            else if(phoneValidCode == 2)
            {
                return getString(R.string.phone_number_message_two);
            }
            else if(phoneValidCode == 3)
            {
                return getString(R.string.phone_number_message_three);
            }
        }
        else if(emailValidCode > 0)
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
        else if(confirmPasswordValidCode > 0)
        {
            if(confirmPasswordValidCode == 1)
            {
                return getString(R.string.password_message_one);
            }
            else if(confirmPasswordValidCode == 2)
            {
                return getString(R.string.confirm_password_message_one);
            }
            else if(confirmPasswordValidCode == 3)
            {
                return getString(R.string.confirm_password_message_two);
            }
            else if(confirmPasswordValidCode == 4)
            {
                return getString(R.string.confirm_password_message_three);
            }
        }
        else if(!isTermsAndConditionsAccept)
        {
            return getString(R.string.accept_term_and_condition_message);
        }

        return null;
    }

    public void launchSignInScreen() {
        ActivityUtils.launchActivity(SignUpActivity.this, SignInActivity.class);
    }
}