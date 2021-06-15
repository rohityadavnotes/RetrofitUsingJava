package com.retrofit.using.java.data.remote;

public class RemoteConfiguration {
    /*
     * Note :  We can set timeouts settings on the underlying HTTP client.
     * If we don’t specify a client, Retrofit will create one with default connect and read timeouts.
     * By default, Retrofit uses the following timeouts :
     *                                                      Connection timeout: 10 seconds
     *                                                      Read timeout: 10 seconds
     *                                                      Write timeout: 10 seconds
     */
    public static final int HTTP_CONNECT_TIMEOUT        = 1;
    public static final int HTTP_READ_TIMEOUT           = 30;
    public static final int HTTP_WRITE_TIMEOUT          = 15;



    /* Network Base Config */
    public static final String HOST_URL                                         = "https://backend24.000webhostapp.com";

    /* Note : If you use Retrofit 2 then add / at the end of base url. if use Retrofit 1 so remove it */
    public static final String BASE_URL                                         = "https://backend24.000webhostapp.com/";

    public static final String API_KEY                                          = "AIzaSyOzB818x55FASHvX4JuGQciR9lv7q";
    public static final String BEARER_AUTHENTICATION_TOKEN                      = "AIzaSyOzB818x55FASHvX4JuGQciR9lv7q";

    /*
     * End points
     * Note : If you use Retrofit 1 then add / at START.
     */
    public static final String EMPLOYEE                                         = "Json/employee.json";
    public static final String EMPLOYEE_LIST                                    = "Json/employeeList.json";

    public static final String UPLOAD                                           = "UploadFile/upload_multipart_file.php";

    public static final String SIGN_UP                                          = "RestApi/middleware/AppSignUp.php";
    public static final String SIGN_IN                                          = "RestApi/middleware/SignIn.php";
    public static final String SOCIAL_SIGN_IN                                   = "RestApi/middleware/SocialSignIn.php";
    public static final String GET_USERS                                        = "RestApi/middleware/GetUsers.php";
}
