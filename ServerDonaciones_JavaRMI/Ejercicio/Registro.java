import java.io.Serializable;

public class Registro implements Serializable{
    private String entidad;
    private double dineroDonacion;

    public Registro(String entidad, double dinero){
        this.entidad = entidad;
        this.dineroDonacion = dinero;
    }

    public String toString(){
        return "[Registro] Entidad " + entidad + " , Dinero donado : " + dineroDonacion ;
    }

    public String getEntidad(){
        return entidad;
    }

    public double getDineroDonacion(){
        return dineroDonacion;
    }
}
