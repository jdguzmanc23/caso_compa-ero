import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            
            System.out.print("Ingrese el nombre del archivo de configuración (por ejemplo config.txt): ");
            String nombreArchivo = scanner.nextLine().trim();

            
            Properties config = new Properties();
            config.load(new FileReader(nombreArchivo));

            
            int N_CLIENTES = Integer.parseInt(config.getProperty("clientes"));
            int N_MENSAJES_POR_CLIENTE = Integer.parseInt(config.getProperty("mensajesPorCliente"));
            int N_FILTROS = Integer.parseInt(config.getProperty("filtros"));
            int N_SERVIDORES = Integer.parseInt(config.getProperty("servidores"));
            int CAPACIDAD_ENTRADA = Integer.parseInt(config.getProperty("capacidadEntrada"));
            int CAPACIDAD_ENTREGA = Integer.parseInt(config.getProperty("capacidadEntrega"));

            
            BuzonEntrada buzonEntrada = new BuzonEntrada(CAPACIDAD_ENTRADA);
            BuzonEntrada buzonCuarentena = new BuzonEntrada(100);
            BuzonEntrega buzonEntrega = new BuzonEntrega(N_SERVIDORES);
            EstadoSistema estado = new EstadoSistema(N_CLIENTES);

           
            Cliente[] clientes = new Cliente[N_CLIENTES];
            for (int i = 0; i < N_CLIENTES; i++) {
                clientes[i] = new Cliente(buzonEntrada, i + 1, N_MENSAJES_POR_CLIENTE);
                clientes[i].start();
            }

            
            FiltroSpam[] filtros = new FiltroSpam[N_FILTROS];
            for (int i = 0; i < N_FILTROS; i++) {
                filtros[i] = new FiltroSpam(buzonEntrada, buzonCuarentena, buzonEntrega, estado);
                filtros[i].start();
            }

           
            ManejadorCuarentena manejador = new ManejadorCuarentena(buzonCuarentena, buzonEntrega);
            manejador.start();

           
            Servidor[] servidores = new Servidor[N_SERVIDORES];
            for (int i = 0; i < N_SERVIDORES; i++) {
                servidores[i] = new Servidor(buzonEntrega, i + 1);
                servidores[i].start();
            }

            
            for (Cliente c : clientes) c.join();
            for (FiltroSpam f : filtros) f.join();
            manejador.join();
            for (Servidor s : servidores) s.join();

         
            System.out.println("\n>>> TODOS LOS PROCESOS FINALIZARON <<<");
            System.out.println("Entrada vacía: " + buzonEntrada.estaVacio());
            System.out.println("Cuarentena vacía: " + buzonCuarentena.estaVacio());
            System.out.println("Entrega vacía: " + buzonEntrega.estaVacio());

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración. Verifica el nombre o su contenido.");
        } catch (NumberFormatException e) {
            System.err.println("Error: el archivo de configuración contiene valores inválidos.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            scanner.close();
        }
    }
}
