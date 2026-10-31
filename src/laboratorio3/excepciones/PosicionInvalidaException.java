package laboratorio3.excepciones;

public final class PosicionInvalidaException extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 1L;

    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }

    public PosicionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje);
        initCause(causa);
    }
}
