public class EstadoSistema {
    private final int totalClientes;
    private int clientesFinalizados;
    private boolean finEnviado;

    public EstadoSistema(int totalClientes) {
        this.totalClientes = totalClientes;
        this.clientesFinalizados = 0;
        this.finEnviado = false;
    }

    public synchronized void clienteFinalizo() {
        clientesFinalizados++;
    }

    public synchronized boolean todosFinalizados() {
        return clientesFinalizados >= totalClientes;
    }

    public synchronized boolean finEnviado() {
        return finEnviado;
    }

    public synchronized void marcarFinEnviado() {
        finEnviado = true;
    }
}
