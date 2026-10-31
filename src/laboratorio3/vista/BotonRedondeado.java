package laboratorio3.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class BotonRedondeado extends JButton {

    private static final long serialVersionUID = 1L;

    private final Color colorFondo;
    private final Color colorHover;
    private final Color colorTexto;

    public BotonRedondeado(String texto, Color colorFondo, Color colorTexto) {
        super(texto);
        this.colorFondo = colorFondo;
        this.colorHover = aclarar(colorFondo, 0.18f);
        this.colorTexto = colorTexto;

        setFont(Estilo.BOTON);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();

            Color fondo = colorFondo;
            if (!isEnabled()) {
                fondo = new Color(90, 98, 115);
            } else if (getModel().isPressed()) {
                fondo = colorFondo.darker();
            } else if (getModel().isRollover()) {
                fondo = colorHover;
            }

            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, ancho, alto, alto, alto);

            if (isFocusOwner()) {
                g2.setColor(new Color(255, 255, 255, 130));
                g2.drawRoundRect(2, 2, ancho - 5, alto - 5, alto, alto);
            }

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = getText();
            g2.setColor(colorTexto);
            g2.drawString(texto, (ancho - fm.stringWidth(texto)) / 2,
                    (alto - fm.getHeight()) / 2 + fm.getAscent());
        } finally {
            g2.dispose();
        }
    }

    private static Color aclarar(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
}
