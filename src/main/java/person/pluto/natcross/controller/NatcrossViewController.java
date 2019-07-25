package person.pluto.natcross.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/natcross")
public class NatcrossViewController {

    @RequestMapping("/natcrossList")
    public String natcrossList() {
        return "natcross/natcrossList";
    }

}
