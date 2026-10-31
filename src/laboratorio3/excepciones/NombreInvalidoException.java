package laboratorio3.excepciones;

public class NombreInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public NombreInvalidoException(String mensaje) {
        super(mensaje);
    }

    public NombreInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
