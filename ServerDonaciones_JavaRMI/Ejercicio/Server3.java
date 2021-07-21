import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Server3 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //Registry reg = LocateRegistry.createRegistry(1099);
            ArrayList<String> array = new ArrayList<>();
            array.add("server1");
            array.add("server2");
            ServidorDonacion server3 = new ServidorDonacion("localhost","server3",array);
            Naming.rebind("server3", server3);
            System.out.println(">> Server3 listo");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println(e.toString());
        }
        
    }    
}