package laboratorio3.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

import laboratorio3.modelo.Carta;

public class BotonCarta extends JButton {

    private static final long serialVersionUID = 1L;

    private final int indice;
    private Carta carta;

    public BotonCarta(int indice) {
        this.indice = indice;
        this.carta = null;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(92, 92));
        setToolTipText("Carta " + (indice + 1));
    }

    public int getIndice() {
        return indice;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();

            if (carta == null) {
                dibujarDorso(g2, ancho, alto);
                return;
            }

            if (carta.estaDescubierta() || carta.estaEmparejada()) {
                try {
                    carta.dibujarCara(g2, ancho, alto);
                } catch (RuntimeException ex) {

                    carta.dibujarRespaldo(g2, ancho, alto);
                }
            } else {
                dibujarDorso(g2, ancho, alto);
            }

            if (carta.estaEmparejada()) {
                g2.setColor(new Color(96, 220, 140, 190));
                g2.setStroke(new BasicStroke(3f));
                g2.draw(new RoundRectangle2D.Float(1.5f, 1.5f, ancho - 3f, alto - 3f, 20, 20));
            }

            if (getModel().isRollover() && !carta.estaEmparejada()) {
                g2.setColor(new Color(255, 203, 5, 170));
                g2.setStroke(new BasicStroke(2.5f));
                g2.draw(new RoundRectangle2D.Float(1.5f, 1.5f, ancho - 3f, alto - 3f, 20, 20));
            }

            if (getModel().isPressed()) {
                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRoundRect(2, 2, ancho - 4, alto - 4, 18, 18);
            }
        } finally {
            g2.dispose();
        }
    }

    private void dibujarDorso(Graphics2D g2, int ancho, int alto) {
        int margen = 3;
        g2.setPaint(new GradientPaint(0, 0, new Color(48, 70, 130), 0, alto, new Color(20, 30, 62)));
        g2.fillRoundRect(margen, margen, ancho - 2 * margen, alto - 2 * margen, 18, 18);

        g2.setColor(new Color(255, 255, 255, 55));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(margen, margen, ancho - 2 * margen, alto - 2 * margen, 18, 18);

        int diametro = (int) (Math.min(ancho, alto) * 0.50);
        int x = (ancho - diametro) / 2;
        int y = (alto - diametro) / 2 - Math.max(2, alto / 20);
        Shape bola = new Ellipse2D.Float(x, y, diametro, diametro);

        Graphics2D recorte = (Graphics2D) g2.create();
        try {
            recorte.clip(bola);
            recorte.setColor(new Color(225, 58, 58));
            recorte.fillRect(x, y, diametro, diametro / 2);
            recorte.setColor(new Color(245, 245, 245));
            recorte.fillRect(x, y + diametro / 2, diametro, diametro / 2);
            recorte.setColor(new Color(25, 32, 50));
            int franja = Math.max(3, diametro / 9);
            recorte.fillRect(x, y + diametro / 2 - franja / 2, diametro, franja);
        } finally {
            recorte.dispose();
        }

        g2.setColor(new Color(25, 32, 50));
        g2.setStroke(new BasicStroke(Math.max(1.5f, diametro / 20f)));
        g2.draw(bola);

        int boton = Math.max(8, diametro / 3);
        Ellipse2D centro = new Ellipse2D.Float(
                ancho / 2f - boton / 2f, y + diametro / 2f - boton / 2f, boton, boton);
        g2.setColor(new Color(248, 248, 248));
        g2.fill(centro);
        g2.setColor(new Color(25, 32, 50));
        g2.draw(centro);

        String signo = "?";
        int tam = Math.max(10, (int) (alto * 0.13));
        g2.setFont(new Font("SansSerif", Font.BOLD, tam));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(new Color(255, 203, 5, 220));
        g2.drawString(signo, (ancho - fm.stringWidth(signo)) / 2,
                y + diametro + Math.max(8, (int) (alto * 0.17)));
    }
}
