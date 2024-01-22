package io.sigco.contactapi.constant;

// Clase que contiene constantes utilizadas en la aplicación
public class Constant {



    // Directorio donde se almacenarán las fotos (se utiliza el directorio de descargas del usuario)
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    // Cabecera HTTP utilizada para indicar el tipo de solicitud (comúnmente asociada con AJAX)
    public static final String X_REQUESTED_WITH = "X-Requested-With";
}
