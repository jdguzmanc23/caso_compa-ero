public class Mensaje {
    private final String tipo;
    private final int idCliente;
    private final String contenido;
    private boolean spam;
    private int tiempoCuarentena;

    public Mensaje(String tipo, int idCliente, String contenido) {
        this.tipo = tipo.toUpperCase();
        this.idCliente = idCliente;
        this.contenido = contenido;
        this.spam = false;
        this.tiempoCuarentena = 0;
    }

    public String getTipo() { return tipo; }
    public int getIdCliente() { return idCliente; }
    public String getContenido() { return contenido; }

    public boolean isSpam() { return spam; }
    public void setSpam(boolean spam) { this.spam = spam; }

    public int getTiempoCuarentena() { return tiempoCuarentena; }
    public void setTiempoCuarentena(int tiempoCuarentena) { this.tiempoCuarentena = tiempoCuarentena; }

    
    public int getClienteId() { return getIdCliente(); }

   
    public void decrementarTiempo() {
        if (tiempoCuarentena > 0) tiempoCuarentena -= 1000;
    }

    @Override
    public String toString() {
        return "[Tipo=" + tipo + ", Cliente=" + idCliente + ", Contenido='" + contenido +
                "', Spam=" + spam + ", Cuarentena=" + tiempoCuarentena + "ms]";
    }
}
