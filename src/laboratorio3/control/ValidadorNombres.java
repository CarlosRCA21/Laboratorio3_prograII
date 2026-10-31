
package laboratorio3.control;

import laboratorio3.excepciones.NombreInvalidoException;

public final class ValidadorNombres {
    public static final int LONGITUD_MAXIMA = 20;

    private ValidadorNombres() {

    }

    public static void validar(String nombreJugador1, String nombreJugador2)
            throws NombreInvalidoException {

        validarNombre(nombreJugador1, "Jugador 1");
        validarNombre(nombreJugador2, "Jugador 2");

        if (nombreJugador1.trim().equalsIgnoreCase(nombreJugador2.trim())) {
            throw new NombreInvalidoException(
                    "Los nombres de ambos jugadores deben ser diferentes.");
        }
    }

    private static void validarNombre(String nombre, String etiqueta) {
        if (nombre == null) {
            throw new NombreInvalidoException("El nombre del " + etiqueta + " no puede ser nulo.");
        }
        String limpio = nombre.trim();
        if (limpio.isEmpty()) {
            throw new NombreInvalidoException(
                    "El nombre del " + etiqueta + " no puede estar vacio ni formado solo por espacios.");
        }
        if (limpio.length() > LONGITUD_MAXIMA) {
            throw new NombreInvalidoException(
                    "El nombre del " + etiqueta + " no puede superar los "
                    + LONGITUD_MAXIMA + " caracteres.");
        }
    }
}