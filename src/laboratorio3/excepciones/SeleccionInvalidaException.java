package laboratorio3.excepciones;

public class SeleccionInvalidaException extends Exception {

    private static final long serialVersionUID = 1L;

    public SeleccionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
