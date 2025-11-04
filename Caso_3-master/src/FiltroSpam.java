import java.util.Random;

public class FiltroSpam extends Thread {
    private final BuzonEntrada buzonEntrada;
    private final BuzonEntrada buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final EstadoSistema estado;
    private final Random random = new Random();

    public FiltroSpam(BuzonEntrada buzonEntrada, BuzonEntrada buzonCuarentena,
                      BuzonEntrega buzonEntrega, EstadoSistema estado) {
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.estado = estado;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje mensaje = buzonEntrada.take();

                switch (mensaje.getTipo()) {
                    case "INICIO":
                        System.out.println("[FiltroSpam] Recibido INICIO del Cliente " + mensaje.getClienteId());
                        continue;

                    case "FIN":
                        estado.clienteFinalizo();
                        System.out.println("[FiltroSpam] Recibido FIN del Cliente " + mensaje.getClienteId());

                        
                        if (estado.todosFinalizados() && !estado.finEnviado()) {
                            estado.marcarFinEnviado();
                            buzonEntrega.put(new Mensaje("FIN", -1, "Fin global de entrega"));
                            buzonCuarentena.put(new Mensaje("FIN", -1, "Fin global cuarentena"));
                            System.out.println("[FiltroSpam] Enviados mensajes de FIN global");
                        }

                       
                        if (estado.todosFinalizados()) {
                            break;
                        }
                        continue;

                    default:
                        
                        if (mensaje.isSpam()) {
                            int tiempoCuarentena = random.nextInt(10000) + 10000;
                            mensaje.setTiempoCuarentena(tiempoCuarentena);
                            buzonCuarentena.put(mensaje);
                            System.out.printf("[FiltroSpam] Cliente %d → SPAM → Cuarentena (%d ms)%n",
                                    mensaje.getClienteId(), tiempoCuarentena);
                        } else {
                            buzonEntrega.put(mensaje);
                            System.out.printf("[FiltroSpam] Cliente %d → VÁLIDO → Entrega%n",
                                    mensaje.getClienteId());
                        }
                        continue;
                }

                
                break;
            }

            System.out.println("[FiltroSpam] Finalizó ejecución.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
