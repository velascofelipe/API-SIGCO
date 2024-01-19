package io.sigco.contactapi.constant;


public class Constant {
    public static final String SECRET = "tuClaveSecreta";
    public static final long EXPIRATION_TIME = 3600000; // 1 hora en milisegundos
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";


    public static final String X_REQUESTED_WITH = "X-Requested-With";
}
