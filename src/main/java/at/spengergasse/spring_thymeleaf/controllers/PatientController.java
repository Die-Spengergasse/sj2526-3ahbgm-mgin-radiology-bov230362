package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Collections;

@Controller
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/patient")
    public String patientStart() {
        return "redirect:/patient/add";
    }

    @GetMapping("/patient/add")
    public String showPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add_patient";
    }


    @GetMapping("/patient/list")
    public String showPatientList(Model model) {
        try {
            model.addAttribute("patients", patientRepository.findAll());
        } catch (DataAccessException e) {
            model.addAttribute("patients", Collections.emptyList());
            model.addAttribute("errorMessage", "Patienten konnten nicht geladen werden. Bitte prüfen, ob MySQL läuft.");
        }
        return "patlist";
    }



    @PostMapping("/patient/add")
    public String savePatient(@ModelAttribute Patient patient, Model model) {

        if (patient.getDateOfBirth() != null && patient.getDateOfBirth().isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Das Geburtsdatum darf nicht in der Zukunft liegen.");
            model.addAttribute("patient", patient);
            return "add_patient";
        }

        if (!isValidAustrianSvnr(patient.getSocialSecurityNumber())) {
            model.addAttribute("errorMessage", "Ungültige Sozialversicherungsnummer.");
            model.addAttribute("patient", patient);
            return "add_patient";
        }

        try {
            patientRepository.save(patient);
            return "redirect:/patient/list";
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "Datenbankzugriff nicht möglich. Bitte prüfen, ob MySQL läuft.");
            model.addAttribute("patient", patient);
            return "add_patient";
        }
    }


    private boolean isValidAustrianSvnr(String svnr) {
        if (svnr == null || !svnr.matches("\\d{10}")) {
            return false;
        }

        int[] d = svnr.chars().map(c -> c - '0').toArray();

        int sum =
                d[0] * 3 + d[1] * 7 + d[2] * 9 + d[4] * 5 + d[5] * 8 + d[6] * 4 + d[7] * 2 + d[8] * 1 + d[9] * 6;
        int checkDigit = sum % 11;

        if (checkDigit == 10) {
            return false;
        }

        return d[3] == checkDigit;
    }
}