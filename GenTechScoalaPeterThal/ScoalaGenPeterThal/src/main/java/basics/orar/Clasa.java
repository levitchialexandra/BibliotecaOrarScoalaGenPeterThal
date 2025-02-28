package basics.orar;


import jakarta.persistence.*;

@Entity
public class Clasa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nume;

    @Enumerated(EnumType.STRING)
    private Ciclu ciclu;
    public enum Ciclu {
        PRIMAR, GIMNAZIAL
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
}
