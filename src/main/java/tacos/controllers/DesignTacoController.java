package tacos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Order;
import tacos.domain.Taco;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design")
@SessionAttributes("order") // keep the model object "order" in session and available
// across multiple requests
public class DesignTacoController {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);

    private final IngredientRepository ingredientRepo;
    private TacoRepository designRepo;

    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "order") // ensures that an Order object will be created in the model
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
//        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {

        // @ModelAttribute on order parameter indicates that its value should come from the model
        // and that Spring MVC should not attempt to bind request parameters to it

        // @Valid will validate the form after it was bound but before the processDesign() is called
        // if there are any validation errors they will be captured in the Errors object and passed
        // to the method

        if (errors.hasErrors()) { // if validation errors - return to the form and show errors
            System.out.println("#########processDesign hasErrors");
            return "design";
        }

        // Save taco design
        Taco saved = designRepo.save(design);
        order.addDesign(saved); // add a new taco design to order object that is kept in session

        log.info("Processing design: " + design);

        return "redirect:/orders/current";
    }

    // only passes the ingredients that are of one of the types declared in the Type enum ?
    // one that is currently iterated over in the for loop

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        List<Ingredient> ing = ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
        System.out.println(ing);
        return ing;
    }
    //filter() takes an object of type Predicate. Predicate takes an object of class Ingredient.
    //lambda is the implementation of the interface method test() of the Predicate interface.
    // test() returns boolean that filter is consuming and either passing the Ingredient or not based
    // on the value that test has returned.

    private List<Ingredient> filterByTypeFor(List<Ingredient> ingredients, Type type) {
        List<Ingredient> result = new ArrayList<>();
        for (Ingredient i : ingredients) {
            if (i.getType().equals(type)) {
                result.add(i);
            }
        }
        return result;
    }

}
