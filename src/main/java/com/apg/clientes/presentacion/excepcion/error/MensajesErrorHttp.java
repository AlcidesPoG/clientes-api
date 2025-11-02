package com.apg.clientes.presentacion.excepcion.error;

public class MensajesErrorHttp {

    private MensajesErrorHttp(){

    }

    public static String obtenerMensajePorCodigo(int codigo) {
        return switch (codigo) {
            case 400 -> "Solicitud invalida";
            case 405 -> "El método HTTP utilizado no está permitido para esta ruta";
            case 406 -> "El formato solicitado no es soportado. Solo se admite JSON";
            case 415 -> "El tipo de contenido enviado no es soportado. Use 'application/json'";
            default -> (codigo >= 400 && codigo < 500)
                    ? "Error en la solicitud del cliente"
                    : "Error interno del servidor";
        };
    }

    public static String obtenerDetallesPorCodigo(int codigo) {
        return switch (codigo) {
            case 400 -> "La solicitud enviada contiene datos inválidos o JSON mal estructurado.";
            case 405 -> "El método HTTP usado no está permitido en esta ruta. Verifique el método correcto (GET, POST, PUT, PATCH o DELETE).";
            case 406 -> "Asegúrese de enviar el encabezado 'Accept: application/json'.";
            case 415 -> "El tipo de contenido del cuerpo no es válido. Use el encabezado 'Content-Type: application/json'.";
            default -> (codigo >= 400 && codigo < 500)
                    ? "Ocurrió un error al procesar la solicitud del cliente. Verifique los datos o la URL."
                    : "Ocurrió un error inesperado";
        };
    }

}
