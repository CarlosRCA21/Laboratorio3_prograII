
package laboratorio3.control;

import java.awt.Color;
import java.util.Random;

import laboratorio3.excepciones.NombreInvalidoException;
import laboratorio3.excepciones.PosicionInvalidaException;
import laboratorio3.excepciones.SeleccionInvalidaException;
import laboratorio3.modelo.Carta;
import laboratorio3.modelo.CartaEspecial;
import laboratorio3.modelo.CartaEstandar;
import laboratorio3.modelo.Jugador;

public class ControladorJuego extends ControlJuego {
        private static final String[] NOMBRES_POKEMON = {
        "Bulbasaur", "Ivysaur", "Venusaur",
        "Charmander", "Charmeleon", "Charizard",
        "Squirtle", "Wartortle", "Blastoise",
        "Pikachu", "Raichu", "Jigglypuff",
        "Gengar", "Onix", "Snorlax",
        "Dragonite", "Mewtwo", "Mew"
    };

    private static final Color[] COLORES = {
        new Color(103, 178, 120), new Color(88, 158, 108), new Color(68, 135, 88),
        new Color(232, 122, 65), new Color(214, 92, 55), new Color(190, 65, 45),
        new Color(86, 156, 214), new Color(64, 126, 188), new Color(45, 98, 158),
        new Color(240, 196, 48), new Color(226, 164, 32), new Color(240, 150, 180),
        new Color(126, 88, 168), new Color(132, 120, 104), new Color(88, 112, 160),
        new Color(232, 148, 72), new Color(176, 96, 190), new Color(242, 140, 170)
    };

    private static final int CANTIDAD_ESPECIALES = 2;

    private final Random aleatorio = new Random();
    private Carta primeraSeleccion;
    private Carta segundaSeleccion;

    @Override
    public void iniciarJuego(String nombreJugador1, String nombreJugador2)
            throws NombreInvalidoException {

        ValidadorNombres.validar(nombreJugador1, nombreJugador2);

        jugadores[0] = new Jugador(nombreJugador1.trim());
        jugadores[1] = new Jugador(nombreJugador2.trim());

        indiceTurnoActual = 0;
        parejasEncontradas = 0;
        bloqueado = false;
        partidaFinalizada = false;
        primeraSeleccion = null;
        segundaSeleccion = null;

        construirTablero();
        mezclarRecursivo(tablero, 0);

        notificarTablero();
        notificarTurno();
    }

    private void construirTablero() {
        for (int i = 0; i < TOTAL_PAREJAS; i++) {
            boolean especial = i >= TOTAL_PAREJAS - CANTIDAD_ESPECIALES;
            Carta prototipo;
            if (especial) {
                prototipo = new CartaEspecial(i, NOMBRES_POKEMON[i], COLORES[i], i);
            } else {
                prototipo = new CartaEstandar(i, NOMBRES_POKEMON[i], COLORES[i], i);
            }

            Carta copia;
            try {
                copia = prototipo.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(
                        "No se pudo clonar la carta de " + NOMBRES_POKEMON[i] + ".", e);
            }

            prototipo.ocultar();
            copia.ocultar();

            tablero[i * 2] = prototipo;
            tablero[i * 2 + 1] = copia;
        }
    }

    private void mezclarRecursivo(Carta[] arreglo, int posicion) {
        if (posicion >= arreglo.length) {
            return;
        }
        int aleatoria = aleatorio.nextInt(posicion + 1);
        Carta temporal = arreglo[posicion];
        arreglo[posicion] = arreglo[aleatoria];
        arreglo[aleatoria] = temporal;
        mezclarRecursivo(arreglo, posicion + 1);
    }

    private int contarEmparejadasRecursivo(Carta[] arreglo, int posicion) {
        if (posicion >= arreglo.length) {
            return 0;
        }
        int actual = (arreglo[posicion] != null && arreglo[posicion].estaEmparejada()) ? 1 : 0;
        return actual + contarEmparejadasRecursivo(arreglo, posicion + 1);
    }

    @Override
    public int contarEmparejadas() {
        return contarEmparejadasRecursivo(tablero, 0);
    }

    @Override
    public void seleccionarCarta(int indice) {
        if (partidaFinalizada) {
            throw new SeleccionInvalidaException("La partida ya finalizo. Inicie una nueva partida.");
        }
        if (bloqueado) {
            throw new SeleccionInvalidaException(
                    "El tablero esta bloqueado. Espere a que se oculten las cartas.");
        }

        Carta carta = getCarta(indice);

        if (carta == null) {
            throw new SeleccionInvalidaException("La posicion seleccionada no contiene una carta.");
        }
        if (carta.estaEmparejada()) {
            throw new SeleccionInvalidaException("Esa pareja ya fue encontrada. Elija otra carta.");
        }
        if (carta == primeraSeleccion) {
            throw new SeleccionInvalidaException("No puede seleccionar la misma carta dos veces.");
        }
        if (carta.estaDescubierta()) {
            throw new SeleccionInvalidaException("Esa carta ya esta descubierta. Elija otra carta.");
        }

        if (primeraSeleccion == null) {
            primeraSeleccion = carta;
            carta.mostrar();
            notificarTablero();
            return;
        }

        segundaSeleccion = carta;
        carta.mostrar();
        notificarTablero();

        if (verificarPareja(primeraSeleccion, segundaSeleccion)) {
            Carta primera = primeraSeleccion;
            Carta segunda = segundaSeleccion;

            primera.marcarEmparejada();
            segunda.marcarEmparejada();

            int puntos = primera.getPuntos();
            Jugador jugador = getJugadorTurno();
            jugador.registrarAcierto(puntos);
            parejasEncontradas++;

            primeraSeleccion = null;
            segundaSeleccion = null;

            notificarTablero();
            notificarTurno();
            if (observador != null) {
                observador.parejaEncontrada(jugador, primera, segunda, puntos);
            }

            if (contarEmparejadas() >= TOTAL_CARTAS) {
                finalizarPartida();
            }
        } else {
            bloqueado = true;
            notificarTablero();
            if (observador != null) {
                observador.parejaFallida(primeraSeleccion, segundaSeleccion);
            }
        }
    }

    @Override
    public boolean verificarPareja(Carta primera, Carta segunda) {
        return primera != null && segunda != null && primera.formaParejaCon(segunda);
    }

    @Override
    public void controlarTurnos() {
        indiceTurnoActual = (indiceTurnoActual + 1) % TOTAL_JUGADORES;
        notificarTurno();
    }

    @Override
    public void cerrarTurnoFallido() {
        if (primeraSeleccion != null) {
            primeraSeleccion.ocultar();
        }
        if (segundaSeleccion != null) {
            segundaSeleccion.ocultar();
        }
        primeraSeleccion = null;
        segundaSeleccion = null;
        bloqueado = false;

        controlarTurnos();
        notificarTablero();
    }

    @Override
    public void finalizarPartida() {
        partidaFinalizada = true;
        bloqueado = true;

        Jugador jugador1 = jugadores[0];
        Jugador jugador2 = jugadores[1];
        boolean empate = jugador1.getPuntaje() == jugador2.getPuntaje();
        Jugador ganador = null;

        if (!empate) {
            ganador = jugador1.getPuntaje() > jugador2.getPuntaje() ? jugador1 : jugador2;
        }

        if (observador != null) {
            observador.partidaFinalizada(ganador, empate);
        }
    }

    @Override
    public Carta getCarta(int indice) {
        try {
            return tablero[indice];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PosicionInvalidaException(
                    "La posicion " + indice + " no existe en el tablero (0 a "
                    + (TOTAL_CARTAS - 1) + ").", e);
        }
    }
}
