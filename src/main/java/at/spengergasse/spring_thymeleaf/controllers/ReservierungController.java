package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.GeraetRepository;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import at.spengergasse.spring_thymeleaf.entities.Reservierung;
import at.spengergasse.spring_thymeleaf.entities.ReservierungRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservierungController {

    private final ReservierungRepository reservierungRepository;
    private final PatientRepository patientRepository;
    private final GeraetRepository geraetRepository;

    public ReservierungController(ReservierungRepository reservierungRepository,
                                  PatientRepository patientRepository,
                                  GeraetRepository geraetRepository) {
        this.reservierungRepository = reservierungRepository;
        this.patientRepository = patientRepository;
        this.geraetRepository = geraetRepository;
    }

    @GetMapping("/reservierung")
    public String form(Model model) {
        model.addAttribute("reservierung", new Reservierung());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("geraete", geraetRepository.findAll());
        return "reservierung-form";
    }

    @PostMapping("/reservierung")
    public String save(@ModelAttribute Reservierung reservierung) {
        reservierungRepository.save(reservierung);
        return "redirect:/reservierung";
    }

    @GetMapping("/reservierung/liste")
    public String geraeteZurAuswahl(Model model) {
        model.addAttribute("geraete", geraetRepository.findAll());
        return "geraete-liste";
    }

    @GetMapping("/geraet/{id}")
    public String liste(@PathVariable Long id, Model model) {
        model.addAttribute("reservierungen", reservierungRepository.findByGeraetId(id));
        return "reservierung-liste";
    }
}