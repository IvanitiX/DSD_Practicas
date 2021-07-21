import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    
    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println("Por favor, especifícame a qué servidor quieres conectarte poniendo el nombre de éste después del nombre de la clase.");
            System.err.println("Estos son los servidores a los que puedes conectarte:");
            try {
                Registry registro = LocateRegistry.getRegistry("localhost", 1099);
                String[] servers = registro.list();
                if (servers.length > 0){
                    for (String string : servers) {
                        System.err.println(string);
                    }
                }
            } catch (RemoteException e) {
                System.err.println("Tenemos otro error : " + e.toString());
            }
            System.exit(-1);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Scanner in = new Scanner(System.in);
        String servidorDestino = args[0];
        String opcion;
        Entidad logged = null;
        boolean identificado, salir;

        try {
            Registry registro = LocateRegistry.getRegistry("localhost", 1099);
            I_Donacion gestor = (I_Donacion)registro.lookup(servidorDestino);
            identificado = false;
            salir = false;

            while (!salir) {
                if (!identificado) {
                    System.out.println("Conectado desde -> " + gestor.getNombre());
                    System.out.println("Bienvenido al sistema. Elige una opción");
                    System.out.println("\t R -> Registrar una entidad");
                    System.out.println("\t L -> Iniciar sesión de una entidad registrada");
                    System.out.println("\t S -> Salir");

                    opcion = in.nextLine();

                    switch (opcion) {
                        case "R":
                            System.out.println("Dime el nombre de la entidad");
                            opcion = in.nextLine();
                            if (!gestor.comprobarRegistroEntidad(opcion)) {
                               gestor.registrarEntidad(opcion); 
                               System.out.println("Se ha registrado la entidad.");
                            } else {
                                System.out.println("La entidad ya existe.");
                            }
                        break;
                        case "L":
                            System.out.println("Dime el nombre de la entidad");
                            opcion = in.nextLine();
                            if (gestor.comprobarRegistroEntidad(opcion)) {
                                logged = gestor.iniciarSesionEntidad(opcion); 
                                System.out.println("Se ha logueado la entidad.");
                                identificado = true;
                            } else {
                                System.out.println("La entidad no existe.");
                            }
                        break;
                        case "S":
                            salir = true;
                            System.out.println("Gracias por usar el servicio de donaciones");
                        break;
                    
                        default:
                            System.out.println("Opción inválida");
                        break;
                    }
                }
                else{
                    System.out.println("Logueado como " + logged.getNombre() +  ". Elige una opción");
                    System.out.println("\t D -> Donar a la causa");
                    System.out.println("\t B -> Mirar cuánto se ha donado en este servidor");
                    System.out.println("\t T -> Mirar cúanto se ha donado en total");
                    System.out.println("\t M -> Mirar el historial de donaciones en este servidor");
                    System.out.println("\t S -> Salir");

                    opcion = in.nextLine();

                    switch (opcion.toUpperCase()) {
                        case "S":
                            salir = true;
                            System.out.println("Gracias por usar el servicio de donaciones");
                        break;

                        case "D":
                            System.out.println("¿Cuánto desea donar a la causa?");
                            double donacion = in.nextDouble();
                            gestor.realizarDonacion(logged.getNombre(), donacion);
                        break;

                        case "B":
                            System.out.println("La entidad " + logged.getNombre() + " ha donado " + gestor.consultarSubtotal(logged.getNombre()) + "€ a la causa en este servidor.");
                        break;

                        case "M":
                            ArrayList<String> lista = gestor.listarRegistros(logged);
                            System.out.println("Host    - Nombre servidor   - Cantidad");
                            System.out.println("______________________________________");
                            for (String string : lista) {
                                System.out.println(string);
                            }
                            gestor.unlockAll();
                        break;

                        case "T":
                            System.out.println("La entidad " + logged.getNombre() + " ha donado " + gestor.consultarTotal(logged.getNombre()) + "€ a la causa.");
                            gestor.unlockAll();
                        break;
                    
                        default:
                            System.out.println("Opción inválida");
                        break;
                    }
                }

                
            }
            
        } catch (NotBoundException | RemoteException e) {
            System.err.println(e.toString());
        }

        in.close();
    }
}
