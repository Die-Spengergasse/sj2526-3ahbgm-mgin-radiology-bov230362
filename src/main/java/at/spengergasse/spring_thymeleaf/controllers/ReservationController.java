package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Collections;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 PatientRepository patientRepository,
                                 DeviceRepository deviceRepository) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/reservation")
    public String showReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        loadFormData(model);
        return "reservation-form";
    }

    @PostMapping("/reservation")
    public String saveReservation(@ModelAttribute Reservation reservation, Model model) {
        try {
            if (reservation.getPatient() != null && reservation.getPatient().getId() != null) {
                Patient patient = patientRepository.findById(reservation.getPatient().getId()).orElse(null);
                reservation.setPatient(patient);
            }

            if (reservation.getDevice() != null && reservation.getDevice().getId() != null) {
                Device device = deviceRepository.findById(reservation.getDevice().getId()).orElse(null);
                reservation.setDevice(device);
            }

            if (reservation.getPatient() == null) {
                model.addAttribute("errorMessage", "Bitte einen Patienten auswählen.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservation.getDevice() == null) {
                model.addAttribute("errorMessage", "Bitte ein Gerät auswählen.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservation.getDateTime() == null) {
                model.addAttribute("errorMessage", "Bitte Datum und Uhrzeit eingeben.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservation.getDateTime().isBefore(LocalDateTime.now())) {
                model.addAttribute("errorMessage", "Eine Reservierung darf nicht in der Vergangenheit liegen.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservation.getBodyRegion() == null || reservation.getBodyRegion().isBlank()) {
                model.addAttribute("errorMessage", "Bitte eine Körperregion eingeben.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservationRepository.existsByDeviceIdAndDateTime(
                    reservation.getDevice().getId(),
                    reservation.getDateTime())) {
                model.addAttribute("errorMessage", "Dieses Gerät ist zu diesem Zeitpunkt bereits reserviert.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            if (reservationRepository.existsByPatientIdAndDateTime(
                    reservation.getPatient().getId(),
                    reservation.getDateTime())) {
                model.addAttribute("errorMessage", "Dieser Patient hat zu diesem Zeitpunkt bereits einen Termin.");
                model.addAttribute("reservation", reservation);
                loadFormData(model);
                return "reservation-form";
            }

            reservationRepository.save(reservation);
            return "redirect:/reservation/list";

        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "Datenbankzugriff nicht möglich. Bitte prüfen, ob MySQL läuft.");
            model.addAttribute("reservation", reservation);
            loadFormData(model);
            return "reservation-form";
        }
    }

    @GetMapping("/reservation/list")
    public String showDeviceList(Model model) {
        try {
            model.addAttribute("devices", deviceRepository.findAll());
        } catch (DataAccessException e) {
            model.addAttribute("devices", Collections.emptyList());
            model.addAttribute("errorMessage", "Geräte konnten nicht geladen werden. Bitte prüfen, ob MySQL läuft.");
        }
        return "device-list";
    }

    @GetMapping("/device/{id}")
    public String showReservationsByDevice(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("reservations", reservationRepository.findByDeviceId(id));
        } catch (DataAccessException e) {
            model.addAttribute("reservations", Collections.emptyList());
            model.addAttribute("errorMessage", "Reservierungen konnten nicht geladen werden. Bitte prüfen, ob MySQL läuft.");
        }
        return "reservation-list";
    }

    private void loadFormData(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
    }
}