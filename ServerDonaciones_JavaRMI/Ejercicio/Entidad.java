import java.io.Serializable;

public class Entidad implements Serializable{
    private String nombre;
    private double totalDonado;

    public Entidad(String nombreEntidad){
        this.nombre = nombreEntidad;
        this.totalDonado = 0.0;
    }

    public String getNombre(){
        return this.nombre;
    }

    public double getTotalDonado(){
        return this.totalDonado;
    }

    public void donarDinero(double dineroADonar){
        this.totalDonado += dineroADonar;
    }

    public String toString(){
        return "[Entidad] Nombre : " + nombre + " Total donado : " + getTotalDonado() + "€.";
    }
}
