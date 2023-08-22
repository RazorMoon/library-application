package com.example.alleywayalliancelms.controller;

import com.example.alleywayalliancelms.model.Checkout;
import com.example.alleywayalliancelms.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("checkout")
    public String showCheckoutPage(Model model) {
        model.addAttribute("checkout", checkoutService.getAllCheckouts());
        return "/checkout/checkPage";
    }

    @PostMapping("checkout")
    public String getCheckoutsByUser(@RequestParam(required = true, defaultValue = "") String accId,
                                     @RequestParam(required = true, defaultValue = "") String action,
                                     RedirectAttributes attributes) {
        if (action.equals("search")) {
            attributes.addFlashAttribute("userCheckoutList", checkoutService.findCheckoutsByAccount(accId));
            attributes.addFlashAttribute("accId", accId);
        }
        return "redirect:/checkout/user";
    }

    @GetMapping("checkout/user")
    public String showCheckoutUserPage(@ModelAttribute("userCheckoutList") ArrayList<Checkout> userCheckoutList,
                                       Model model) {
        model.addAttribute("checkList", userCheckoutList);
        return "checkout/userchecks";
    }

    @PostMapping("/checkout/user")
    public String alterCheckoutUserPage(@RequestParam(required = true, defaultValue = "") Boolean returnFlag,
                                        @RequestParam(required = true, defaultValue = "") String action,
                                        @RequestParam(required = true, defaultValue = "") Long copyId) {
        if (action.equals("alter")) {
            checkoutService.checkoutComplete(copyId, returnFlag);
        }
        return "redirect:/checkout";
    }


}
