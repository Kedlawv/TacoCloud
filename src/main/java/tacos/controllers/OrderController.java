package tacos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.domain.Order;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/current")
    public String orderForm(Model model){
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    @PostMapping("/current")
    public String processOrder(Order order){
        log.info("Order submitted: " + order);
        return "redirect:/";
    }

}
