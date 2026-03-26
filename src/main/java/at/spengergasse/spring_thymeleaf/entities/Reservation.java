package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Device device;

    private LocalDateTime time;
    private String bodyregion;
    private String comment;

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Device getGeraet() {
        return device;
    }

    public void setGeraet(Device geraet) {
        this.device = geraet;
    }

    public LocalDateTime getZeit() {
        return time;
    }

    public void setZeit(LocalDateTime zeit) {
        this.time = time;
    }

    public String getKoerperregion() {
        return bodyregion;
    }

    public void setKoerperregion(String koerperregion) {
        this.bodyregion = bodyregion;
    }

    public String getKommentar() {
        return comment;
    }

    public void setKommentar(String kommentar) {
        this.comment = comment;
    }
}