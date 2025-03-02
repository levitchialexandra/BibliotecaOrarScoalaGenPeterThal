package ro.scoalaPeterThal;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import basics.AppUser;
import basics.orar.Clasa;
import basics.orar.Clasa.Ciclu;
import basics.orar.Orar;
import jakarta.servlet.http.HttpSession;
import repository.ClasaRepository;
import repository.OrarRepository;
import repository.UserRepository;

@Controller
public class AdminController {
    @Autowired
    UserRepository appUserRepo;
    @Autowired
    private HttpSession session;


     @Autowired
    OrarRepository orarRepository;

    @Autowired
    ClasaRepository clasaRepository;
    @GetMapping("/pages/login")
    public String loginPage() {

        return "pages/adminlogin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/pages/adminlogin";
    }

    @PostMapping("/pages/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        try {
            AppUser appUser = loginUser(username, password);
            session.setAttribute("sessionUser", appUser);

            return "redirect:/pages/admindashboard";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/adminlogin";
        }
    }

    public AppUser loginUser(String username, String password) {
        AppUser appUser = appUserRepo.findByUsername(username).orElse(null);

        if (appUser == null) {
            throw new RuntimeException("Utilizatorul nu a fost găsit");
        }

        if (!appUser.checkPassword(password)) {
            throw new RuntimeException("Parolă incorectă");
        }

        return appUser;
    }

    @GetMapping("/pages/adminorar")
    public String adminOrarPage(@RequestParam(required = false, defaultValue = "PRIMAR") Ciclu ciclu,
            @RequestParam(required = false) String clasa, Model model) {
        List<Orar> orar = Collections.emptyList();
        if (clasa != null) {
            orar = orarRepository.findByClasaNume(clasa);
        }
        List<String> colors = Arrays.asList("#AF9FF1", "#FFED89", "#7EAEEC", "#E4FDB2", "#FEAAD8");
        List<Clasa> clase = clasaRepository.findByCiclu(ciclu);
        List<String> zile = List.of("Luni", "Marți", "Miercuri", "Joi", "Vineri");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<String> distinctOrarOre = orar.stream()
                .map(orarEntry -> orarEntry.getOraInceput().format(timeFormatter)
                        + " - " + orarEntry.getOraSfarsit().format(timeFormatter))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("colors", colors);
        model.addAttribute("zile", zile);
        model.addAttribute("orarOre", distinctOrarOre);
        model.addAttribute("clase", clase);
        model.addAttribute("orar", orar);
        model.addAttribute("cicluSelectat", ciclu.name());

        model.addAttribute("clasaSelectata", clasa);
        return "pages/adminorar";
    }

    @PostMapping("/check-password")
    public ResponseEntity<Map<String, Boolean>> checkPassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        Map<String, Boolean> response = new HashMap<>();
        try {
            String username = "oraruser";
            loginUser(username, password);
            
                response.put("success", true);
                return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            response.put("success", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
