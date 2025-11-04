import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrega {
    private final Queue<Mensaje> cola = new LinkedList<>();
    private final int totalServidores;
    private int servidoresNotificados = 0;
    private boolean finRecibido = false;

    public BuzonEntrega(int totalServidores) {
        this.totalServidores = totalServidores;
    }

    public synchronized void put(Mensaje mensaje) {
        cola.add(mensaje);
        notifyAll();
    }

    public synchronized Mensaje take() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        Mensaje mensaje = cola.poll();
        if (mensaje.getTipo().equals("FIN")) {
            servidoresNotificados++;
            if (servidoresNotificados < totalServidores) {
                cola.add(mensaje);
                notifyAll();
            } else {
                finRecibido = true;
            }
        }
        return mensaje;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }

    public synchronized boolean finRecibido() {
        return finRecibido;
    }
}
