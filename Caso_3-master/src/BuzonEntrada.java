import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {
    private final int capacidad;
    private final Queue<Mensaje> cola = new LinkedList<>();

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
    }
    
    public synchronized void put(Mensaje mensaje) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(mensaje);
        notify(); 
    }

    public synchronized Mensaje take() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        Mensaje mensaje = cola.poll();
        notify(); 
        return mensaje;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
}
