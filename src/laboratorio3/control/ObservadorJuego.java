
package laboratorio3.control;

import laboratorio3.modelo.Carta;
import laboratorio3.modelo.Jugador;

public abstract class ObservadorJuego {

    public void tableroActualizado() {
    }

    public void parejaEncontrada(Jugador jugador, Carta primera, Carta segunda, int puntos) {
    }

    public void parejaFallida(Carta primera, Carta segunda) {
    }

    public void turnoCambiado(Jugador jugador) {
    }

    public void partidaFinalizada(Jugador ganador, boolean empate) {
    }

    public void error(String mensaje) {
    }
}
