import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Cliente_Ejemplo {
public static void main(String args[]) {
    if(System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
    }
    try{
        String nombre_objeto_remoto = "Ejemplo_I";
        System.out.println("Buscando el objeto remoto");
        Registry registry = LocateRegistry.getRegistry(args[0]);
        Ejemplo_I instancia_local = (Ejemplo_I) registry.lookup(nombre_objeto_remoto);
        System.out.println("Invocando el objeto remoto");
        instancia_local.escribir_mensaje(Integer.parseInt(args[1]));
    }
        catch(Exception e) {
            System.err.println("Ejemplo_I exception:");
            e.printStackTrace();
        }
    }
}

/*
 ¿Qué hace?
    El cliente solicita al servidor usar el método remoto escribir_mensaje.
    Para ello primero mira si se ha establecido algún tipo de seguridad en el cliente. De no ser así se le crea uno.
    Luego mira el rmiregistry en busca del objeto Ejemplo_I de la clase Ejemplo_I para crear una instancia local.
    Por último se llama al método desde la instancia local de ese objeto.

    El método escribir_mensaje mira el número del proceso que pasamos como argumento al cliente. Si es 0 hace una espera de 5 segundos. En cualquier caso, imprimirá el número de hebra.
 

*/