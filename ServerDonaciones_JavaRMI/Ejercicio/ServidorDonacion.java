import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServidorDonacion extends UnicastRemoteObject implements I_Donacion {
    private ArrayList<Entidad> entidades;
    private ArrayList<Registro> registros;
    private boolean bloqueado;
    private String host;
    private String nombreSvPrincipal;
    private ArrayList<Replica> replicas;

    @Override
    public String getNombre() throws RemoteException{
        return nombreSvPrincipal;
    }

    public ServidorDonacion(String host, String svPrincipal, ArrayList<String> svReplicas) throws RemoteException{
        this.host = host;
        this.nombreSvPrincipal = svPrincipal;
        replicas = new ArrayList<>();
        entidades = new ArrayList<>();
        registros = new ArrayList<>();
        bloqueado = false;
        for (String string : svReplicas) {
            replicas.add(new Replica(string));
        }
    }

    @Override
    public Replica getReplica(Replica replica) throws RemoteException{
        I_Donacion buscada = null;
        Replica replicante = replica;
        try {
            Registry registro = LocateRegistry.getRegistry(host,1099);
            buscada = (I_Donacion)registro.lookup(replica.getNombre());
            if(buscada != null){
                replicante.setInterfaz(buscada);
                for (Replica rep : replicas) {
                    if(rep.getNombre().equals(replicante.getNombre())){
                        rep = replicante;
                    }
                }
            }
        } catch (RemoteException | NotBoundException e) {
            System.out.println(e.toString());
        }

        return replicante;
    }


    @Override
    public boolean isBloqueado(){
        return bloqueado;
    }

    @Override
    public void setBloqueado(boolean locked){
        bloqueado = locked;
    }

    @Override
    public void registrarEntidad(String entidad) throws RemoteException{
        boolean registrada = false;
        registrada = comprobarRegistroEntidad(entidad) ;
        unlockAll();
        if(!registrada){
            I_Donacion servidorRegistro = this;
            int numRegistrados = entidades.size();
            for (Replica replica : replicas) {
                this.getReplica(replica);
                if (replica.getInterfaz().getNumEntidades() < numRegistrados) {
                    numRegistrados = replica.getInterfaz().getNumEntidades();
                    servidorRegistro = replica.getInterfaz();
                }
            }

            if(servidorRegistro == this){
                Entidad nueva = new Entidad(entidad);
                entidades.add(nueva);
                System.out.println("Se ha creado la entidad -> " + nueva.toString());
            }
            else{
                servidorRegistro.registrarEntidad(entidad);
            }
        }
    }

    @Override
    public Entidad iniciarSesionEntidad(String entidad) throws RemoteException{
        if(comprobarRegistroEntidad(entidad)){
            System.out.println("Iniciando sesión con " + entidad);
            return buscarEntidad(entidad);
        }
        else return null;
    }

    @Override
    public void realizarDonacion(String entidad, double cantidad) throws RemoteException{
        if(comprobarRegistroEntidad(entidad) && cantidad > 0.0){
            Entidad resultado = buscarEntidad(entidad);
            System.out.println(resultado.getNombre() + " ha donado " + cantidad + "€ a la causa ");
            resultado.donarDinero(cantidad);
            this.modificarEntidad(resultado);
            this.unlockAll();
            registros.add(new Registro(entidad, cantidad));

        } 
        else if (cantidad < 0.0){
            System.err.println("[!] Han intentado sacar dinero de una causa benéfica >:(");
        }
    }

    @Override
    public boolean comprobarRegistroEntidad(String entidad) throws RemoteException{
        return (buscarEntidad(entidad) != null);
    }

    @Override
    public Entidad buscarEntidad(String entidad) throws RemoteException{
        Entidad encontrada = null;

        if (!entidades.isEmpty()) {
            for (Entidad entity : entidades) {
                if (entity.getNombre().equals(entidad)){
                    encontrada = entity;
                }
            }
        }

        if(encontrada == null){
            System.out.println("[!] No he encontrado la entidad " + entidad +  " en mi server.");
            setBloqueado(true);
            if(!replicas.isEmpty()){
                for (Replica replica : replicas) {
                    this.getReplica(replica);
                    if (encontrada == null && !replica.getInterfaz().isBloqueado()) {
                       encontrada = replica.getInterfaz().buscarEntidad(entidad); 
                    }
                }
            }
        }
        return encontrada;
    }

    @Override
    public synchronized void modificarEntidad(Entidad entidad) throws RemoteException{
        boolean encontrada = false;

        if (!entidades.isEmpty()) {
            for (Entidad entity : entidades) {
                if (entity.getNombre().equals(entidad.getNombre())){
                    entidades.remove(entidades.indexOf(entity));
                    entidades.add(entidad);
                }
            }
        }

        if(encontrada == false){
            setBloqueado(true);
            if(!replicas.isEmpty()){
                for (Replica replica : replicas) {
                    this.getReplica(replica);
                    if (encontrada == false && !replica.getInterfaz().isBloqueado()) {
                       replica.getInterfaz().modificarEntidad(entidad); 
                    }
                }
            }
        }
    }

    @Override
    public double consultarSubtotal(String entidad) throws RemoteException{
        for (Replica rep : replicas) {
            getReplica(rep);
        }
        if(comprobarRegistroEntidad(entidad)){
            Entidad resultado = buscarEntidad(entidad);
            System.out.println(resultado.toString());
            if (!registros.isEmpty()) {
                double subtotal = 0.0;
                for (Registro reg : registros) {
                    System.out.println(reg.toString());
                    if (reg.getEntidad().trim().equals(resultado.getNombre().trim())) {
                        System.out.println("Registro hallado");
                        subtotal += reg.getDineroDonacion();
                    }
                }
                return subtotal;
            } else {
                return 0.0;
            }
        }
        else return 0.0;
    }

    @Override
    public double consultarTotal(String entidad) throws RemoteException{ 
        if(comprobarRegistroEntidad(entidad)){
            return this.buscarEntidad(entidad).getTotalDonado();
        }
        else return 0.0;
    }

    @Override
    public void unlockAll() throws RemoteException{
        this.setBloqueado(false);
        for (Replica replica : replicas) {
            replica.getInterfaz().setBloqueado(false);
        }
    }

    @Override
    public int getNumEntidades() throws RemoteException{
        return entidades.size();
    }

    @Override
    public boolean hayRegistros(String entidad) throws RemoteException{
        boolean existeRegistro = false;
        for (int i = 0; i < registros.size() && !existeRegistro; i++) {
            if(registros.get(i).getEntidad().equals(entidad)) existeRegistro = true;
        }
        return existeRegistro;
    }


    @Override
    public ArrayList<String> listarRegistros(Entidad entidad) throws RemoteException{
        ArrayList<String> listaRegistros = new ArrayList<>();
        if(!registros.isEmpty()){
            for (Registro reg : registros) {
                if(reg.getEntidad().equals(entidad.getNombre())){
                    listaRegistros.add(host + "  " + nombreSvPrincipal + " " + reg.getDineroDonacion());
                }
            }
        }

        setBloqueado(true);

        return listaRegistros;
    }
}
