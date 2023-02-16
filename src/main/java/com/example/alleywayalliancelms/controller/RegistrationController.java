package com.example.alleywayalliancelms.controller;

import com.example.alleywayalliancelms.model.PatronAccount;
import com.example.alleywayalliancelms.service.PatronAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/***
 * A controller class that is used for handling user requests for pages relating to Registration {@link PatronAccount}.
 * The views for this pages may be checked here:
 *
 * <ul>
 *     <li><a href="file:../resources/templates/registration/regPage.html"> Patron Account Registration Page</a></li>
 * </ul>
 */

@Slf4j
@Controller
@RequestMapping("/")
public class RegistrationController {

    private final PatronAccountService patronAccountService;

    @Autowired
    public RegistrationController(PatronAccountService patronAccountService) {
        this.patronAccountService = patronAccountService;
    }

    @GetMapping("registration")
    public String showRegistration(Model model) {
        model.addAttribute("userForm", new PatronAccount());
        return "registration/regPage";
    }

    @PostMapping("registration")
    public String addUser(@ModelAttribute("userForm") @Valid PatronAccount userForm,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            return "registration/regPage";
        }

        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration/regPage";
        }

        if (!patronAccountService.savePatronAccount(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
        }

        return "redirect:/";

    }
}