import java.util.Random;

public class Servidor extends Thread {
    private final BuzonEntrega buzonEntrega;
    private final int id;
    private final Random random = new Random();

    public Servidor(BuzonEntrega buzonEntrega, int id) {
        this.buzonEntrega = buzonEntrega;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje mensaje = buzonEntrega.take();
                if (mensaje.getTipo().equals("FIN")) {
                    System.out.println("[Servidor " + id + "] Recibió FIN. Terminando ejecución.");
                    break;
                }
                Thread.sleep(random.nextInt(1000) + 500);
                System.out.println("[Servidor " + id + "] Procesó mensaje: " + mensaje.getContenido());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
