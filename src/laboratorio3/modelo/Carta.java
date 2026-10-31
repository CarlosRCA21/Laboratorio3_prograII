package laboratorio3.modelo;


public abstract class Carta implements Cloneable {

    private int idPareja = 0;
    private String nombre = "";
    private boolean descubierta = false;
    private boolean emparejada = false;

    public Carta(int idPareja, String nombre) {
        this.idPareja = idPareja;
        this.nombre = nombre;
    }

    public void mostrar() {
        descubierta = true;
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
        emparejada = true;
        descubierta = true;
    }

    public boolean estaEmparejada() {
        return emparejada;
    }

    public int getIdPareja() {
        return idPareja;
    }

    public String getNombre() {
        return nombre;
    }

   
    public boolean esEspecial() {
        return false;
    }

   
    public abstract int obtenerPuntos();

    
    @Override
    public Carta clone() throws CloneNotSupportedException {
        Carta copia = (Carta) super.clone();
        copia.descubierta = false;
        copia.emparejada = false;
        return copia;
    }
}
