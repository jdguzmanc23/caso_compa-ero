import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ManejadorCuarentena extends Thread {
    private final BuzonEntrada buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final Random random = new Random();
    private final List<Mensaje> cuarentena = new CopyOnWriteArrayList<>();

    public ManejadorCuarentena(BuzonEntrada buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() {
        try {
            while (true) {
                
                if (!buzonCuarentena.estaVacio()) {
                    Mensaje msg = buzonCuarentena.take();
                    if (msg.getTipo().equals("FIN")) {
                        System.out.println("[ManejadorCuarentena] Recibió FIN. Terminando ejecución.");
                        break;
                    }
                    cuarentena.add(msg);
                }

                
                List<Mensaje> paraEliminar = new ArrayList<>();

                for (Mensaje msg : cuarentena) {
                    msg.decrementarTiempo();

                    if (msg.getTiempoCuarentena() <= 0) {
                        int r = random.nextInt(21) + 1;

                        if (r % 7 == 0) {
                            System.out.println("[ManejadorCuarentena] DESCARTÓ mensaje malicioso del Cliente "
                                    + msg.getClienteId());
                        } else {
                            buzonEntrega.put(msg);
                            System.out.println("[ManejadorCuarentena] Liberó mensaje del Cliente "
                                    + msg.getClienteId() + " → Enviado a Entrega");
                        }

                        paraEliminar.add(msg);
                    }
                }

                
                cuarentena.removeAll(paraEliminar);

                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
