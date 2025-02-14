package models;

import jakarta.persistence.*;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ville;

    private String pays;

    private ClimatEnum climat;

    public Destination() {}

    public Destination(String ville, String pays, ClimatEnum climat) {
        this.ville = ville;
        this.pays = pays;
        this.climat = climat;
    }

    public Long getId() {
        return id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public ClimatEnum getClimat() {
        return climat;
    }

    public void setClimat(ClimatEnum climat) {
        this.climat = climat;
    }
}
