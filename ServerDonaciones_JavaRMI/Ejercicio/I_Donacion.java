import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface I_Donacion extends Remote {
    //Definiremos aquí qué métodos se usarán en clases que gestionen las donaciones.
    public void registrarEntidad(String entidad) throws RemoteException;
    public String getNombre() throws RemoteException;
    public Entidad iniciarSesionEntidad(String entidad) throws RemoteException;
    public Entidad buscarEntidad(String entidad) throws RemoteException;
    public boolean comprobarRegistroEntidad(String entidad) throws RemoteException;
    public void realizarDonacion(String entidad, double cantidad) throws RemoteException;
    public double consultarTotal(String entidad) throws RemoteException;
    public double consultarSubtotal(String entidad) throws RemoteException;
    public int getNumEntidades() throws RemoteException;
    public boolean isBloqueado() throws RemoteException;
    public void setBloqueado(boolean locked) throws RemoteException;
    public void unlockAll() throws RemoteException;
    public Replica getReplica(Replica replica) throws RemoteException;
    public boolean hayRegistros(String entidad) throws RemoteException;
    public void modificarEntidad(Entidad entidad) throws RemoteException;
    public ArrayList<String> listarRegistros(Entidad entidad) throws RemoteException;
}
