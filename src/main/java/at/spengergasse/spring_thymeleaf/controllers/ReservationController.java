package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import at.spengergasse.spring_thymeleaf.entities.Reservation;
import at.spengergasse.spring_thymeleaf.entities.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "reservation-form";
    }

    @PostMapping("/reservation")
    public String saveReservation(@ModelAttribute Reservation reservation) {
        reservationRepository.save(reservation);
        return "redirect:/reservation/list";
    }

    @GetMapping("/reservation/list")
    public String showDeviceList(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "device-list";
    }

    @GetMapping("/device/{id}")
    public String showReservationsByDevice(@PathVariable Long id, Model model) {
        model.addAttribute("reservations", reservationRepository.findByDeviceId(id));
        return "reservation-list";
    }
}