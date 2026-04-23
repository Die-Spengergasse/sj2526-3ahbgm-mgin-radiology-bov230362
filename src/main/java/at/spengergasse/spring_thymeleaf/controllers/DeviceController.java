package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/device")
    public String showDevices(Model model) {
        try {
            model.addAttribute("devices", deviceRepository.findAll());
        } catch (DataAccessException e) {
            model.addAttribute("devices", Collections.emptyList());
            model.addAttribute("errorMessage", "Geräte konnten nicht geladen werden. Bitte prüfen, ob MySQL läuft.");
        }
        return "device-list";
    }
}