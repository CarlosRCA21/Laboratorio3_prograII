
package laboratorio3.modelo;


public class CartaEstandar extends Carta {
      public CartaEstandar(int idPareja, String nombre) {
        super(idPareja, nombre);
    }

    @Override
    public int obtenerPuntos() {
        return 1;
    }
}
