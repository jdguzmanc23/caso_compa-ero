import java.util.Random;

public class Cliente extends Thread {
    private final BuzonEntrada buzon;
    private final int id;
    private final int cantidadMensajes;
    private final Random random = new Random();

    public Cliente(BuzonEntrada buzon, int id, int cantidadMensajes) {
        this.buzon = buzon;
        this.id = id;
        this.cantidadMensajes = cantidadMensajes;
    }

    @Override
    public void run() {
        try {
            buzon.put(new Mensaje("INICIO", id, "Inicio de envío"));
            System.out.println("[Cliente " + id + "] INICIO envío");

            for (int i = 1; i <= cantidadMensajes; i++) {
                Mensaje mensaje = new Mensaje("NORMAL", id, "Mensaje " + i + " del Cliente " + id);
                mensaje.setSpam(random.nextBoolean());
                buzon.put(mensaje);
                Thread.sleep(random.nextInt(200) + 100);
            }

            buzon.put(new Mensaje("FIN", id, "Fin de envío"));
            System.out.println("[Cliente " + id + "] FIN envío");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
