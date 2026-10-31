package laboratorio3.modelo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class CartaEspecial extends Carta {

    public CartaEspecial(int idPareja, String nombrePokemon, Color colorBase, int variante) {
        super(idPareja, nombrePokemon, colorBase, variante);
    }

    @Override
    public int getPuntos() {
        return 2;
    }

    @Override
    public boolean esEspecial() {
        return true;
    }

    @Override
    protected void dibujarDetalle(Graphics2D g, int ancho, int alto) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {

            g2.setColor(new Color(255, 200, 40));
            g2.setStroke(new BasicStroke(Math.max(2.5f, ancho / 26f)));
            g2.draw(new RoundRectangle2D.Float(5, 5, ancho - 10, alto - 10, 16, 16));

            double cx = ancho * 0.80;
            double cy = alto * 0.20;
            double rExterior = Math.min(ancho, alto) * 0.075;
            double rInterior = rExterior * 0.45;
            Path2D estrella = new Path2D.Double();
            for (int i = 0; i < 10; i++) {
                double radio = (i % 2 == 0) ? rExterior : rInterior;
                double angulo = -Math.PI / 2 + i * Math.PI / 5;
                double x = cx + Math.cos(angulo) * radio;
                double y = cy + Math.sin(angulo) * radio;
                if (i == 0) {
                    estrella.moveTo(x, y);
                } else {
                    estrella.lineTo(x, y);
                }
            }
            estrella.closePath();
            g2.setColor(new Color(255, 215, 0));
            g2.fill(estrella);
            g2.setColor(new Color(150, 100, 0));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(estrella);
        } finally {
            g2.dispose();
        }
    }
}
