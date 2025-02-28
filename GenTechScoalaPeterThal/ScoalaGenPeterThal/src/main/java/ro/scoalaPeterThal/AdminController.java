package ro.scoalaPeterThal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import basics.AppUser;
import jakarta.servlet.http.HttpSession;
import repository.UserRepository;

@Controller
public class AdminController {
    @Autowired
    UserRepository appUserRepo;
    @Autowired
    private HttpSession session;

    @GetMapping("/pages/login")
    public String loginPage() {

        return "pages/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/pages/login";
    }

    @PostMapping("/pages/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        try {
            AppUser appUser = loginUser(username, password);
            session.setAttribute("sessionUser", appUser);

            return "redirect:/pages/addBook";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/login";
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
