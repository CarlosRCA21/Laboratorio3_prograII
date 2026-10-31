package laboratorio3.modelo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public abstract class Carta implements Cloneable {

    private final int idPareja;
    private final String nombrePokemon;
    private final Color colorBase;
    private final int variante;

    private boolean descubierta;
    private boolean emparejada;

    protected Carta(int idPareja, String nombrePokemon, Color colorBase, int variante) {
        this.idPareja = idPareja;
        this.nombrePokemon = nombrePokemon;
        this.colorBase = colorBase;
        this.variante = variante;
        this.descubierta = false;
        this.emparejada = false;
    }

    public final int getIdPareja() {
        return idPareja;
    }

    public final String getNombrePokemon() {
        return nombrePokemon;
    }

    public final Color getColorBase() {
        return colorBase;
    }

    public final int getVariante() {
        return variante;
    }

    public void mostrar() {
        if (!emparejada) {
            descubierta = true;
        }
    }

    public void ocultar() {
        if (!emparejada) {
            descubierta = false;
        }
    }

    public boolean estaDescubierta() {
        return descubierta;
    }

    public void marcarEmparejada() {
        this.emparejada = true;
        this.descubierta = true;
    }

    public boolean estaEmparejada() {
        return emparejada;
    }

    public abstract int getPuntos();

    public abstract boolean esEspecial();

    public boolean formaParejaCon(Carta otra) {
        return otra != null && otra != this && this.idPareja == otra.idPareja;
    }

    @Override
    public Carta clone() throws CloneNotSupportedException {
        Carta copia = (Carta) super.clone();
        copia.descubierta = false;
        copia.emparejada = false;
        return copia;
    }

    public final void dibujarCara(Graphics2D g, int ancho, int alto) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int margen = Math.max(3, Math.min(ancho, alto) / 22);
            RoundRectangle2D fondo = new RoundRectangle2D.Float(
                    margen, margen, ancho - 2 * margen, alto - 2 * margen, 16, 16);

            Color claro = aclarar(colorBase, 0.62f);
            g2.setPaint(new GradientPaint(0, 0, claro, 0, alto, colorBase));
            g2.fill(fondo);

            g2.setColor(new Color(255, 255, 255, 105));
            g2.setStroke(new BasicStroke(Math.max(1f, ancho / 90f)));
            g2.draw(new RoundRectangle2D.Float(
                    margen + 2, margen + 2,
                    ancho - 2 * margen - 4, alto - 2 * margen - 4, 12, 12));

            g2.setColor(new Color(255, 255, 255, 80));
            g2.fill(new Ellipse2D.Float(ancho * 0.18f, alto * 0.10f, ancho * 0.64f, alto * 0.58f));

            dibujarCriatura(g2, ancho, alto);
            dibujarDetalle(g2, ancho, alto);
            dibujarNombre(g2, ancho, alto);
        } finally {
            g2.dispose();
        }
    }

    protected void dibujarDetalle(Graphics2D g, int ancho, int alto) {

    }

    private void dibujarCriatura(Graphics2D g2, int ancho, int alto) {
        int cx = ancho / 2;
        int cy = (int) (alto * 0.39);
        int radio = (int) (Math.min(ancho, alto) * 0.155);
        int forma = variante % 3;

        g2.setStroke(new BasicStroke(Math.max(1.2f, ancho / 70f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.setColor(new Color(255, 255, 255, 235));
        if (forma == 0) {
            Path2D orejas = new Path2D.Float();
            orejas.moveTo(cx - radio * 0.95, cy - radio * 0.35);
            orejas.lineTo(cx - radio * 1.35, cy - radio * 1.55);
            orejas.lineTo(cx - radio * 0.15, cy - radio * 0.95);
            orejas.closePath();
            orejas.moveTo(cx + radio * 0.95, cy - radio * 0.35);
            orejas.lineTo(cx + radio * 1.35, cy - radio * 1.55);
            orejas.lineTo(cx + radio * 0.15, cy - radio * 0.95);
            orejas.closePath();
            g2.fill(orejas);
            g2.setColor(new Color(35, 45, 65));
            g2.draw(orejas);
        } else if (forma == 1) {
            Ellipse2D izquierda = new Ellipse2D.Float(cx - radio * 1.5f, cy - radio * 1.35f,
                    radio * 0.85f, radio * 0.9f);
            Ellipse2D derecha = new Ellipse2D.Float(cx + radio * 0.65f, cy - radio * 1.35f,
                    radio * 0.85f, radio * 0.9f);
            g2.fill(izquierda);
            g2.fill(derecha);
            g2.setColor(new Color(35, 45, 65));
            g2.draw(izquierda);
            g2.draw(derecha);
        } else {
            g2.fill(new Ellipse2D.Float(cx - radio * 1.7f, cy - radio * 1.0f,
                    radio * 1.1f, radio * 0.55f));
            g2.fill(new Ellipse2D.Float(cx + radio * 0.6f, cy - radio * 1.0f,
                    radio * 1.1f, radio * 0.55f));
        }

        Ellipse2D cuerpo = new Ellipse2D.Float(cx - radio * 1.28f, cy - radio * 0.8f,
                radio * 2.56f, radio * 2.1f);
        g2.setColor(new Color(255, 255, 255, 235));
        g2.fill(cuerpo);
        g2.setColor(new Color(35, 45, 65));
        g2.draw(cuerpo);

        int manchas = variante % 4;
        g2.setColor(new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 165));
        for (int i = 0; i < manchas; i++) {
            double angulo = Math.PI * 0.25 + i * (Math.PI * 0.55);
            float mx = (float) (cx + Math.cos(angulo) * radio * 0.85);
            float my = (float) (cy + Math.sin(angulo) * radio * 1.05);
            g2.fill(new Ellipse2D.Float(mx - radio * 0.22f, my - radio * 0.22f,
                    radio * 0.44f, radio * 0.44f));
        }

        g2.setColor(new Color(35, 45, 65));
        if (variante % 2 == 0) {
            g2.fill(new Ellipse2D.Float(cx - radio * 0.72f, cy - radio * 0.28f,
                    radio * 0.30f, radio * 0.48f));
            g2.fill(new Ellipse2D.Float(cx + radio * 0.42f, cy - radio * 0.28f,
                    radio * 0.30f, radio * 0.48f));
        } else {
            g2.fill(new Ellipse2D.Float(cx - radio * 0.78f, cy - radio * 0.30f,
                    radio * 0.46f, radio * 0.28f));
            g2.fill(new Ellipse2D.Float(cx + radio * 0.32f, cy - radio * 0.30f,
                    radio * 0.46f, radio * 0.28f));
        }

        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Float(cx - radio * 0.64f, cy - radio * 0.22f,
                radio * 0.10f, radio * 0.14f));
        g2.fill(new Ellipse2D.Float(cx + radio * 0.50f, cy - radio * 0.22f,
                radio * 0.10f, radio * 0.14f));

        g2.setColor(new Color(35, 45, 65));
        g2.draw(new Arc2D.Float(cx - radio * 0.42f, cy + radio * 0.28f,
                radio * 0.84f, radio * 0.62f, 200, 140, Arc2D.OPEN));
    }

    private void dibujarNombre(Graphics2D g2, int ancho, int alto) {
        String nombre = nombrePokemon.toUpperCase();
        int tam = Math.max(8, (int) (alto * 0.088));
        Font fuente = new Font("SansSerif", Font.BOLD, tam);
        g2.setFont(fuente);
        FontMetrics fm = g2.getFontMetrics();
        while (fm.stringWidth(nombre) > ancho * 0.84 && tam > 6) {
            tam--;
            fuente = new Font("SansSerif", Font.BOLD, tam);
            g2.setFont(fuente);
            fm = g2.getFontMetrics();
        }

        int textoAncho = fm.stringWidth(nombre);
        int bandaAlto = fm.getHeight() + Math.max(2, alto / 40);
        int bandaY = alto - bandaAlto - Math.max(3, alto / 26);
        RoundRectangle2D banda = new RoundRectangle2D.Float(
                Math.max(3, ancho * 0.06f), bandaY,
                ancho - Math.max(6, ancho * 0.12f), bandaAlto,
                bandaAlto, bandaAlto);

        g2.setColor(new Color(20, 28, 48, 185));
        g2.fill(banda);
        g2.setColor(Color.WHITE);
        g2.drawString(nombre, (ancho - textoAncho) / 2,
                bandaY + (bandaAlto - fm.getHeight()) / 2 + fm.getAscent());
    }

    public final void dibujarRespaldo(Graphics2D g, int ancho, int alto) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(250, 250, 245));
            g2.fillRoundRect(3, 3, ancho - 6, alto - 6, 16, 16);
            g2.setColor(new Color(180, 40, 40));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(3, 3, ancho - 6, alto - 6, 16, 16);

            String nombre = nombrePokemon;
            int tam = Math.max(8, (int) (alto * 0.08));
            g2.setFont(new Font("SansSerif", Font.BOLD, tam));
            FontMetrics fm = g2.getFontMetrics();
            while (fm.stringWidth(nombre) > ancho * 0.82 && tam > 6) {
                tam--;
                g2.setFont(new Font("SansSerif", Font.BOLD, tam));
                fm = g2.getFontMetrics();
            }
            g2.setColor(new Color(40, 45, 60));
            g2.drawString(nombre, (ancho - fm.stringWidth(nombre)) / 2,
                    alto / 2 + fm.getAscent() / 2);
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
