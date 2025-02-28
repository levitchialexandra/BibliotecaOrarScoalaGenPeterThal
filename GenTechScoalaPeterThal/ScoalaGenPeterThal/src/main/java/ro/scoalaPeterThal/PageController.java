package ro.scoalaPeterThal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "/index";
    }

    @GetMapping("/index")
    public String homePage() {
        return "/index";
    }

    @GetMapping("/pages/biblioteca-digitala")
    public String bibliotecaDigitala() {
        return "pages/biblioteca-digitala";
    }

    @GetMapping("/pages/despre-noi")
    public String about() {
        return "pages/despre-noi";
    }

    @GetMapping("/pages/ghidul-elevului")
    public String ghidulElevului() {
        return "pages/ghidul-elevului";
    }

    @GetMapping("/pages/inscrieri")
    public String inscrieri() {
        return "pages/inscrieri";
    }

    @GetMapping("/pages/istoricul-scolii")
    public String istoriculScolii() {
        return "pages/istoricul-scolii";
    }

    @GetMapping("/pages/noutati")
    public String noutati() {
        return "pages/noutati";
    }

    @GetMapping("/pages/oferta-educationala")
    public String ofertaEducationala() {
        return "pages/oferta-educationala";
    }

    @GetMapping("/pages/contact")
    public String contact() {
        return "pages/contact";
    }

    @GetMapping("/pages/postare")
    public String postare() {
        return "pages/postare";
    }

    @GetMapping("/pages/politica-cookie")
    public String politicaCookie() {
        return "pages/politica-cookie";
    }

    @GetMapping("/pages/termeni-si-conditii")
    public String termeniSiConditii() {
        return "pages/termeni-si-conditii";
    }
}
