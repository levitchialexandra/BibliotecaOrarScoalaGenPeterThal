package ro.scoalaPeterThal;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import basics.orar.Clasa;
import basics.orar.Orar;
import basics.orar.Clasa.Ciclu;
import repository.ClasaRepository;
import repository.MaterieRepository;
import repository.OrarRepository;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrarController {

    @Autowired
    OrarRepository orarRepository;

    @Autowired
    ClasaRepository clasaRepository;

   

    @GetMapping("/pages/orar")
    public String afiseazaOrar(@RequestParam(required = false, defaultValue = "PRIMAR") Ciclu ciclu,
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
       
        return "pages/orar";
        
    }
}
