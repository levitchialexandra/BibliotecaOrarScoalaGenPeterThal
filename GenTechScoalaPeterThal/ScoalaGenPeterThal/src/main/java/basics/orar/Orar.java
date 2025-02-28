package basics.orar;


import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
public class Orar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Clasa clasa;

    @ManyToOne
    private Materie materie;

    @ManyToOne
    private Profesor profesor;

    
    private String ziua;

    private LocalTime oraInceput;
    private LocalTime oraSfarsit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clasa getClasa() {
        return clasa;
    }

    public void setClasa(Clasa clasa) {
        this.clasa = clasa;
    }

    public Materie getMaterie() {
        return materie;
    }

    public void setMaterie(Materie materie) {
        this.materie = materie;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getZiua() {
        return ziua;
    }

    public void setZiua(String ziua) {
        this.ziua = ziua;
    }

    public LocalTime getOraInceput() {
        return oraInceput;
    }

    public void setOraInceput(LocalTime oraInceput) {
        this.oraInceput = oraInceput;
    }

    public LocalTime getOraSfarsit() {
        return oraSfarsit;
    }

    public void setOraSfarsit(LocalTime oraSfarsit) {
        this.oraSfarsit = oraSfarsit;
    }

}
