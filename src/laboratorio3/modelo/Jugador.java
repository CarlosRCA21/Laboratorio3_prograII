package laboratorio3.modelo;



public class Jugador {

    public static final int MAXIMO_LETRAS = 15;

    private String nombre = "";
    private int aciertos = 0;
    private int puntaje = 0;

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getPuntaje() {
        return puntaje;
    }

    // Suma un acierto y los puntos de la pareja encontrada
    public void sumarPareja(int puntos) {
        aciertos = aciertos + 1;
        puntaje = puntaje + puntos;
    }

    // Se usa al empezar una partida nueva
    public void reiniciar() {
        aciertos = 0;
        puntaje = 0;
    }

    // Revisa los dos nombres. Si alguno está mal, lanza la excepción propia
    // con un mensaje que dice exactamente qué hay que corregir.
    public static void validarNombres(String nombre1, String nombre2) throws NombreInvalidoException {
        validarNombre(nombre1, "Jugador 1");
        validarNombre(nombre2, "Jugador 2");

        if (nombre1.trim().equalsIgnoreCase(nombre2.trim())) {
            throw new NombreInvalidoException(
                    "Los dos jugadores tienen el mismo nombre. Cambie el nombre del Jugador 2.");
        }
    }

    private static void validarNombre(String nombre, String etiqueta) throws NombreInvalidoException {
        if (nombre == null || nombre.length() == 0) {
            throw new NombreInvalidoException("Falta el nombre del " + etiqueta + ".");
        }
        if (nombre.trim().length() == 0) {
            throw new NombreInvalidoException(
                    "El nombre del " + etiqueta + " solo tiene espacios. Escriba un nombre.");
        }
        if (nombre.trim().length() > MAXIMO_LETRAS) {
            throw new NombreInvalidoException("El nombre del " + etiqueta
                    + " es muy largo (máximo " + MAXIMO_LETRAS + " caracteres).");
        }
    }
}
