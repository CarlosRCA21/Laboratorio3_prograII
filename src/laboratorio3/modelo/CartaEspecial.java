
package laboratorio3.modelo;


public class CartaEspecial extends Carta {

    public CartaEspecial(int idPareja, String nombre) {
        super(idPareja, nombre);
    }

    @Override
    public int obtenerPuntos() {
        return 2;
    }

    @Override
    public boolean esEspecial() {
        return true;
    }
}
