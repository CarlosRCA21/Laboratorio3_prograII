package laboratorio3.modelo;

public class Jugador {

    private final String nombre;
    private int aciertos;
    private int puntaje;
    private long tiempoAcumuladoMs;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.aciertos = 0;
        this.puntaje = 0;
        this.tiempoAcumuladoMs = 0L;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public long getTiempoAcumuladoMs() {
        return tiempoAcumuladoMs;
    }

    public void registrarAcierto(int puntos) {
        aciertos++;
        puntaje += puntos;
    }

    public void sumarTiempo(long milisegundos) {
        if (milisegundos > 0) {
            tiempoAcumuladoMs += milisegundos;
        }
    }

    public String getTiempoFormateado() {
        long totalSegundos = tiempoAcumuladoMs / 1000L;
        long minutos = totalSegundos / 60L;
        long segundos = totalSegundos % 60L;
        return String.format("%02d:%02d", minutos, segundos);
    }

    public void reiniciar() {
        aciertos = 0;
        puntaje = 0;
        tiempoAcumuladoMs = 0L;
    }
}
