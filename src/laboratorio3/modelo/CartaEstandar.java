package laboratorio3.modelo;

import java.awt.Color;

public class CartaEstandar extends Carta {

    public CartaEstandar(int idPareja, String nombrePokemon, Color colorBase, int variante) {
        super(idPareja, nombrePokemon, colorBase, variante);
    }

    @Override
    public int getPuntos() {
        return 1;
    }

    @Override
    public boolean esEspecial() {
        return false;
    }
}
