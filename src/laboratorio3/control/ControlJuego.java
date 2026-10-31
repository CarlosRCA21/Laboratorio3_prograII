package laboratorio3.control;

import laboratorio3.excepciones.NombreInvalidoException;
import laboratorio3.modelo.Carta;
import laboratorio3.modelo.Jugador;

public abstract class ControlJuego {

    public static final int FILAS = 6;
    public static final int COLUMNAS = 6;
    public static final int TOTAL_CARTAS = FILAS * COLUMNAS;
    public static final int TOTAL_PAREJAS = TOTAL_CARTAS / 2;
    public static final int TOTAL_JUGADORES = 2;

    protected final Carta[] tablero = new Carta[TOTAL_CARTAS];
    protected final Jugador[] jugadores = new Jugador[TOTAL_JUGADORES];

    protected ObservadorJuego observador;
    protected int indiceTurnoActual;
    protected int parejasEncontradas;
    protected boolean bloqueado;
    protected boolean partidaFinalizada;

    public abstract void iniciarJuego(String nombreJugador1, String nombreJugador2)
            throws NombreInvalidoException;

    public abstract void controlarTurnos();

    public abstract boolean verificarPareja(Carta primera, Carta segunda);

    public abstract void finalizarPartida();

    public abstract void seleccionarCarta(int indice);

    public abstract void cerrarTurnoFallido();

    public abstract Carta getCarta(int indice);

    public abstract int contarEmparejadas();

    public void setObservador(ObservadorJuego observador) {
        this.observador = observador;
    }

    public Jugador getJugadorTurno() {
        return jugadores[indiceTurnoActual];
    }

    public Jugador getJugador(int indice) {
        if (indice < 0 || indice >= jugadores.length) {
            throw new IndexOutOfBoundsException("Indice de jugador invalido: " + indice);
        }
        return jugadores[indice];
    }

    public int getIndiceTurno() {
        return indiceTurnoActual;
    }

    public int getParejasEncontradas() {
        return parejasEncontradas;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public boolean isPartidaFinalizada() {
        return partidaFinalizada;
    }

    public void acumularTiempoTurno(long milisegundos) {
        if (!partidaFinalizada && milisegundos > 0) {
            getJugadorTurno().sumarTiempo(milisegundos);
        }
    }

    protected void notificarTablero() {
        if (observador != null) {
            observador.tableroActualizado();
        }
    }

    protected void notificarTurno() {
        if (observador != null) {
            observador.turnoCambiado(getJugadorTurno());
        }
    }

}
