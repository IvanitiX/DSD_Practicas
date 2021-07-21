public class Replica{
    private String nombre;
    private I_Donacion interfazReplica;

    public Replica(){}

    public Replica(String nombre){
        this.nombre = nombre;
    }

    public Replica(String nombre, I_Donacion server){
        this.interfazReplica = server;
        this.nombre = nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setInterfaz(I_Donacion interfazReplica){
        this.interfazReplica = interfazReplica;
    }

    public String getNombre(){
        return this.nombre;
    }

    public I_Donacion getInterfaz(){
        return this.interfazReplica;
    }
}
