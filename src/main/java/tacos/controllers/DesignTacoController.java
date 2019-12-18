package tacos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.IngredientRepository;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Taco;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design")
public class DesignTacoController {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);

    private final IngredientRepository ingredientRepo;

    public DesignTacoController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

//    @ModelAttribute
//    public void addIngriedentsToModel(Model model){
//        List<Ingredient> ingredients = Arrays.asList(
//                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//        );
//
//        Type[] types = Ingredient.Type.values();
//        for(Type type : types){
//            model.addAttribute(type.toString().toLowerCase(),
//                    filterByTypeFor(ingredients,type));
//        }
//    }


    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for(Type type : types){
            model.addAttribute(type.toString().toLowerCase(),filterByType(ingredients,type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors){
        //@ModelAttribute fixed the crash after submitting the form with errors
        // I'm guessing that after an error we do not run the showDesignForm() again we simply go straight
        // to the "design" view and we no longer have access to the design object ?
        // and annotating the Taco design with @ModelAttribute pushed the design object back to design view?

        // @Valid will validate the form after it was bound but before the processDesign() is called
        // if there are any validation errors they will be captured in the Errors object and passed
        // to the method

        if(errors.hasErrors()){ // if validation errors - return to the form and show errors
            return "design";
        }

        // Save taco design
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

    private List<Ingredient> filterByTypeFor(List<Ingredient> ingredients, Type type){
        List<Ingredient> result = new ArrayList<>();
        for(Ingredient i : ingredients){
            if(i.getType().equals(type)){
                result.add(i);
            }
        }
        return result;
    }

}
