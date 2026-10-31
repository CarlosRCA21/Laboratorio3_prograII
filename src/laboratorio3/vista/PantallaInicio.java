package laboratorio3.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import laboratorio3.control.ControlJuego;
import laboratorio3.control.ControladorJuego;
import laboratorio3.control.ValidadorNombres;
import laboratorio3.excepciones.NombreInvalidoException;

public class PantallaInicio extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField campoJugador1;
    private final JTextField campoJugador2;

    public PantallaInicio() {
        super("Memoria Pokemon - Inicio");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        campoJugador1 = crearCampo();
        campoJugador2 = crearCampo();

        JPanel contenido = new PanelFondo(new BorderLayout(0, 18));
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 42, 28, 42));

        contenido.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.add(crearFormulario(), BorderLayout.CENTER);
        contenido.add(crearPie(), BorderLayout.SOUTH);

        setContentPane(contenido);
        setSize(620, 540);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("MEMORIA POKEMON", SwingConstants.CENTER);
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.ACENTO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Actividad #3 - Laboratorio de Programacion II", SwingConstants.CENTER);
        subtitulo.setFont(Estilo.SUBTITULO);
        subtitulo.setForeground(Estilo.TEXTO_SUAVE);
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel instrucciones = new JLabel(
                "<html><div style='text-align:center; width:470px;'>"
                + "Ingrese el nombre de cada jugador. El tablero tiene 36 cartas: "
                + "16 parejas valen 1 punto y 2 parejas especiales (borde dorado) valen 2 puntos."
                + "</div></html>", SwingConstants.CENTER);
        instrucciones.setFont(Estilo.TEXTO_NORMAL);
        instrucciones.setForeground(Estilo.TEXTO_SUAVE);
        instrucciones.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(16));
        panel.add(instrucciones);
        return panel;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(10, 24, 0, 24));

        formulario.add(crearFila("Nombre del Jugador 1", campoJugador1));
        formulario.add(Box.createVerticalStrut(14));
        formulario.add(crearFila("Nombre del Jugador 2", campoJugador2));
        return formulario;
    }

    private JPanel crearFila(String etiqueta, JTextField campo) {
        JPanel fila = new JPanel(new BorderLayout(0, 7));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));

        JLabel label = new JLabel(etiqueta);
        label.setFont(Estilo.ETIQUETA);
        label.setForeground(Estilo.TEXTO);

        fila.add(label, BorderLayout.NORTH);
        fila.add(campo, BorderLayout.CENTER);
        return fila;
    }

    private JPanel crearPie() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        BotonRedondeado boton = new BotonRedondeado(
                "INICIAR PARTIDA", Estilo.ACENTO, new Color(28, 33, 48));
        boton.setPreferredSize(new Dimension(220, 48));
        boton.addActionListener(e -> iniciarPartida());

        JPanel contenedorBoton = new JPanel(new GridLayout(1, 1));
        contenedorBoton.setOpaque(false);
        contenedorBoton.setBorder(BorderFactory.createEmptyBorder(0, 160, 0, 160));
        contenedorBoton.add(boton);

        JLabel pie = new JLabel(
                "18 parejas  |  6 x 6  |  2 jugadores  |  cronometro individual",
                SwingConstants.CENTER);
        pie.setFont(Estilo.SUBTITULO);
        pie.setForeground(Estilo.TEXTO_SUAVE);

        panel.add(contenedorBoton, BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(boton);
        return panel;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        campo.setForeground(Estilo.TEXTO);
        campo.setCaretColor(Estilo.ACENTO);
        campo.setBackground(new Color(23, 32, 57));
        campo.setSelectionColor(new Color(70, 100, 160));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE, 1, true),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        return campo;
    }

    private void iniciarPartida() {
        String nombre1 = campoJugador1.getText();
        String nombre2 = campoJugador2.getText();

        try {
            ValidadorNombres.validar(nombre1, nombre2);

            ControlJuego control = new ControladorJuego();
            PantallaTablero tablero = new PantallaTablero(control, nombre1.trim(), nombre2.trim());
            tablero.setVisible(true);
            dispose();

        } catch (NombreInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            enfocarCampoConError(ex.getMessage());
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo iniciar la partida:\n" + ex.getMessage(),
                    "Error inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enfocarCampoConError(String mensaje) {
        if (mensaje != null && mensaje.contains("Jugador 1")) {
            campoJugador1.requestFocusInWindow();
            campoJugador1.selectAll();
        } else {
            campoJugador2.requestFocusInWindow();
            campoJugador2.selectAll();
        }
    }

    private static class PanelFondo extends JPanel {

        private static final long serialVersionUID = 1L;

        PanelFondo(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int ancho = getWidth();
                int alto = getHeight();

                g2.setPaint(new GradientPaint(0, 0, new Color(25, 35, 66), ancho, alto,
                        new Color(10, 14, 28)));
                g2.fillRect(0, 0, ancho, alto);

                g2.setColor(new Color(255, 255, 255, 9));
                int diametro = Math.max(ancho, alto);
                g2.fillOval(ancho - diametro / 2, -diametro / 3, diametro, diametro);
                g2.fillOval(-diametro / 2, alto - diametro / 2, diametro, diametro);
            } finally {
                g2.dispose();
            }
        }
    }
}
