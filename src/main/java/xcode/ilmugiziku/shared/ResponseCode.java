package xcode.ilmugiziku.shared;

public class ResponseCode {
    //ERROR
    public static final String FAILED_MESSAGE = "Failed to connect to database";
    public static final String NOT_FOUND_MESSAGE = "Data not found";
    public static final String PARAMS_ERROR_MESSAGE = "Wrong params";
    public static final String EXIST_MESSAGE = "Data exist";
    public static final int FAILED_CODE = 90;
    public static final int EXIST_CODE = 99;
    public static final int NOT_FOUND_CODE = 404;
    public static final int PARAMS_CODE = 101;

    //SUCCESS
    public static final String SUCCESS_MESSAGE = "success";
    public static final int SUCCESS_CODE = 20;
}
