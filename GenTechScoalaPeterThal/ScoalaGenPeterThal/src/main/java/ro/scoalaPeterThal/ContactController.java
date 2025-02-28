package ro.scoalaPeterThal;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {
  

    // Această metodă va prelua datele din formular
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("message") String message) {
        // Creăm un mesaj simplu de tip email
       
        return "Mesajul tău a fosts trimis!";
    }
}
