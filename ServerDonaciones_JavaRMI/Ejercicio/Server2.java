import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Server2 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //Registry reg = LocateRegistry.createRegistry(1099);
            ArrayList<String> array = new ArrayList<>();
            array.add("server1");
            array.add("server3");
            ServidorDonacion server2 = new ServidorDonacion("localhost","server2",array);
            Naming.rebind("server2", server2);
            System.out.println(">> Server2 listo");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println(e.toString());
        }
        
    }    
}
