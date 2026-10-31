package laboratorio3.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import laboratorio3.control.ControlJuego;
import laboratorio3.control.ObservadorJuego;
import laboratorio3.excepciones.NombreInvalidoException;
import laboratorio3.excepciones.PosicionInvalidaException;
import laboratorio3.excepciones.SeleccionInvalidaException;
import laboratorio3.modelo.Carta;
import laboratorio3.modelo.Jugador;

public class PantallaTablero extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int RETARDO_PAUSA_MS = 1000;
    private static final int INTERVALO_RELOJ_MS = 100;

    private final ControlJuego control;
    private final String nombreJugador1;
    private final String nombreJugador2;

    private final BotonCarta[] botones = new BotonCarta[ControlJuego.TOTAL_CARTAS];
    private final JPanel[] panelesJugador = new JPanel[ControlJuego.TOTAL_JUGADORES];
    private final JLabel[] lblAciertos = new JLabel[ControlJuego.TOTAL_JUGADORES];
    private final JLabel[] lblPuntaje = new JLabel[ControlJuego.TOTAL_JUGADORES];
    private final JLabel[] lblTiempo = new JLabel[ControlJuego.TOTAL_JUGADORES];

    private JLabel lblTurno;
    private JLabel lblEstado;
    private Timer reloj;
    private Timer pausa;
    private long ultimoTickNs;
    private boolean dialogFinalMostrado;

    public PantallaTablero(ControlJuego control, String nombreJugador1, String nombreJugador2) {
        super("Memoria Pokemon - Tablero");
        this.control = control;
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.dialogFinalMostrado = false;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        construirUI();
        control.setObservador(crearObservador());

        try {
            control.iniciarJuego(nombreJugador1, nombreJugador2);
        } catch (NombreInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo preparar el tablero:\n" + ex.getMessage(),
                    "Error inesperado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                detenerRelojes();
            }
        });

        iniciarReloj();
        actualizarVista();

        setSize(840, 900);
        setMinimumSize(new Dimension(720, 760));
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setBackground(Estilo.FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));

        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(crearTablero(), BorderLayout.CENTER);
        contenido.add(crearPie(), BorderLayout.SOUTH);

        setContentPane(contenido);
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout(0, 10));
        encabezado.setOpaque(false);

        JPanel filaTitulo = new JPanel(new BorderLayout());
        filaTitulo.setOpaque(false);

        JLabel titulo = new JLabel("MEMORIA POKEMON");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.ACENTO);

        lblTurno = new JLabel("Turno de: -", SwingConstants.RIGHT);
        lblTurno.setFont(Estilo.TURNO);
        lblTurno.setForeground(Estilo.JUGADOR_1);

        filaTitulo.add(titulo, BorderLayout.WEST);
        filaTitulo.add(lblTurno, BorderLayout.EAST);

        JPanel jugadores = new JPanel(new GridLayout(1, 2, 14, 0));
        jugadores.setOpaque(false);
        jugadores.add(crearPanelJugador(0));
        jugadores.add(crearPanelJugador(1));

        encabezado.add(filaTitulo, BorderLayout.NORTH);
        encabezado.add(jugadores, BorderLayout.CENTER);
        return encabezado;
    }

    private JPanel crearPanelJugador(int indice) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(Estilo.PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE, 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        String nombre = indice == 0 ? nombreJugador1 : nombreJugador2;
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));
        lblNombre.setForeground(indice == 0 ? Estilo.JUGADOR_1 : Estilo.JUGADOR_2);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAciertos[indice] = crearEtiquetaEstadistica("Aciertos: 0");
        lblPuntaje[indice] = crearEtiquetaEstadistica("Puntaje: 0 pts");
        lblTiempo[indice] = crearEtiquetaEstadistica("Tiempo: 00:00");

        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblAciertos[indice]);
        panel.add(lblPuntaje[indice]);
        panel.add(lblTiempo[indice]);

        panelesJugador[indice] = panel;
        return panel;
    }

    private JLabel crearEtiquetaEstadistica(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Estilo.TEXTO_NORMAL);
        etiqueta.setForeground(Estilo.TEXTO);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private JPanel crearTablero() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Estilo.PANEL);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel cuadricula = new JPanel(new GridLayout(
                ControlJuego.FILAS, ControlJuego.COLUMNAS, 6, 6));
        cuadricula.setOpaque(false);

        for (int i = 0; i < botones.length; i++) {
            final int indice = i;
            BotonCarta boton = new BotonCarta(i);
            boton.addActionListener(e -> seleccionarCarta(indice));
            botones[i] = boton;
            cuadricula.add(boton);
        }

        contenedor.add(cuadricula, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new BorderLayout(16, 0));
        pie.setOpaque(false);

        lblEstado = new JLabel("Seleccione dos cartas para buscar una pareja.");
        lblEstado.setFont(Estilo.TEXTO_NORMAL);
        lblEstado.setForeground(Estilo.TEXTO_SUAVE);

        BotonRedondeado btnNueva = new BotonRedondeado(
                "NUEVA PARTIDA", new Color(74, 96, 150), Estilo.TEXTO);
        btnNueva.setPreferredSize(new Dimension(180, 42));
        btnNueva.addActionListener(e -> solicitarNuevaPartida());

        pie.add(lblEstado, BorderLayout.CENTER);
        pie.add(btnNueva, BorderLayout.EAST);
        return pie;
    }

    private ObservadorJuego crearObservador() {
        return new ObservadorJuego() {

            @Override
            public void tableroActualizado() {
                actualizarVista();
            }

            @Override
            public void parejaEncontrada(Jugador jugador, Carta primera, Carta segunda, int puntos) {
                lblEstado.setForeground(Estilo.VERDE);
                lblEstado.setText("Pareja de " + primera.getNombrePokemon()
                        + ": +" + puntos + " punto(s) para " + jugador.getNombre() + ".");
            }

            @Override
            public void parejaFallida(Carta primera, Carta segunda) {
                lblEstado.setForeground(Estilo.ROJO);
                lblEstado.setText("No coinciden. Memorice su posicion...");
                iniciarPausa();
            }

            @Override
            public void turnoCambiado(Jugador jugador) {
                lblEstado.setForeground(Estilo.TEXTO_SUAVE);
                lblEstado.setText("Turno de " + jugador.getNombre() + ".");
                actualizarVista();
            }

            @Override
            public void partidaFinalizada(Jugador ganador, boolean empate) {
                manejarFinPartida(ganador, empate);
            }

            @Override
            public void error(String mensaje) {
                JOptionPane.showMessageDialog(PantallaTablero.this, mensaje,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private void seleccionarCarta(int indice) {
        if (control.isPartidaFinalizada() || control.isBloqueado()) {
            return;
        }
        try {
            control.seleccionarCarta(indice);
        } catch (SeleccionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Seleccion no valida", JOptionPane.WARNING_MESSAGE);
        } catch (PosicionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Posicion no valida", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error inesperado:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarPausa() {
        if (pausa != null && pausa.isRunning()) {
            pausa.stop();
        }
        pausa = new Timer(RETARDO_PAUSA_MS, e -> {
            ((Timer) e.getSource()).stop();
            if (!control.isPartidaFinalizada()) {
                control.cerrarTurnoFallido();
            }
        });
        pausa.setRepeats(false);
        pausa.start();
    }

    private void iniciarReloj() {
        ultimoTickNs = System.nanoTime();
        reloj = new Timer(INTERVALO_RELOJ_MS, e -> {
            long ahora = System.nanoTime();
            long transcurridoMs = (ahora - ultimoTickNs) / 1_000_000L;
            ultimoTickNs = ahora;

            if (!control.isPartidaFinalizada()) {
                control.acumularTiempoTurno(transcurridoMs);
            }
            actualizarTiempos();
        });
        reloj.start();
    }

    private void detenerRelojes() {
        if (reloj != null) {
            reloj.stop();
        }
        if (pausa != null) {
            pausa.stop();
        }
    }

    private void actualizarVista() {
        if (control.getJugador(0) == null) {
            return;
        }

        for (int i = 0; i < botones.length; i++) {
            botones[i].setCarta(control.getCarta(i));
        }

        actualizarEstadisticas();

        for (int i = 0; i < panelesJugador.length; i++) {
            boolean activo = control.getIndiceTurno() == i && !control.isPartidaFinalizada();
            panelesJugador[i].setBackground(activo ? Estilo.PANEL_ACTIVO : Estilo.PANEL);
            panelesJugador[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(activo ? Estilo.ACENTO : Estilo.BORDE, 2, true),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        }

        Jugador turno = control.getJugadorTurno();
        lblTurno.setText("Turno de: " + turno.getNombre());
        lblTurno.setForeground(control.getIndiceTurno() == 0
                ? Estilo.JUGADOR_1 : Estilo.JUGADOR_2);
    }

    private void actualizarEstadisticas() {
        for (int i = 0; i < ControlJuego.TOTAL_JUGADORES; i++) {
            Jugador jugador = control.getJugador(i);
            if (jugador == null) {
                continue;
            }
            lblAciertos[i].setText("Aciertos: " + jugador.getAciertos());
            lblPuntaje[i].setText("Puntaje: " + jugador.getPuntaje() + " pts");
            lblTiempo[i].setText("Tiempo: " + jugador.getTiempoFormateado());
        }
    }

    private void actualizarTiempos() {
        for (int i = 0; i < ControlJuego.TOTAL_JUGADORES; i++) {
            Jugador jugador = control.getJugador(i);
            if (jugador != null) {
                lblTiempo[i].setText("Tiempo: " + jugador.getTiempoFormateado());
            }
        }
    }

    private void manejarFinPartida(Jugador ganador, boolean empate) {
        if (dialogFinalMostrado) {
            return;
        }
        dialogFinalMostrado = true;

        detenerRelojes();
        actualizarVista();

        Jugador jugador1 = control.getJugador(0);
        Jugador jugador2 = control.getJugador(1);

        String mensaje;
        if (empate) {
            mensaje = "Empate!\n\n"
                    + "Ambos jugadores obtuvieron " + jugador1.getPuntaje() + " puntos.\n\n"
                    + jugador1.getNombre() + ": " + jugador1.getAciertos()
                    + " aciertos, tiempo " + jugador1.getTiempoFormateado() + "\n"
                    + jugador2.getNombre() + ": " + jugador2.getAciertos()
                    + " aciertos, tiempo " + jugador2.getTiempoFormateado();
        } else {
            mensaje = "Ganador: " + ganador.getNombre() + "!\n\n"
                    + "Puntaje final: " + ganador.getPuntaje() + " puntos\n"
                    + "Aciertos: " + ganador.getAciertos() + " parejas\n"
                    + "Tiempo acumulado: " + ganador.getTiempoFormateado() + "\n\n"
                    + jugador1.getNombre() + ": " + jugador1.getPuntaje()
                    + " puntos, tiempo " + jugador1.getTiempoFormateado() + "\n"
                    + jugador2.getNombre() + ": " + jugador2.getPuntaje()
                    + " puntos, tiempo " + jugador2.getTiempoFormateado();
        }

        int opcion = JOptionPane.showOptionDialog(this, mensaje, "Fin de la partida",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[] { "Jugar de nuevo", "Salir" }, "Jugar de nuevo");

        if (opcion == 0) {
            reiniciarPartida();
        } else {
            dispose();
            System.exit(0);
        }
    }

    private void solicitarNuevaPartida() {
        if (control.isPartidaFinalizada()) {
            reiniciarPartida();
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "Desea reiniciar la partida y mezclar el tablero?",
                "Nueva partida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            reiniciarPartida();
        }
    }

    private void reiniciarPartida() {
        detenerRelojes();
        try {
            control.iniciarJuego(nombreJugador1, nombreJugador2);
            dialogFinalMostrado = false;
            lblEstado.setForeground(Estilo.TEXTO_SUAVE);
            lblEstado.setText("Nueva partida. Seleccione dos cartas.");
            actualizarVista();
            iniciarReloj();
        } catch (NombreInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos invalidos", JOptionPane.WARNING_MESSAGE);
        }
    }
}
