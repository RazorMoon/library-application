package com.example.alleywayalliancelms.controller;

import com.example.alleywayalliancelms.model.PatronAccount;
import com.example.alleywayalliancelms.service.PatronAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class PatronAccountController {

    private final PatronAccountService patronAccountService;

    @GetMapping("account/{name}")
    public String getPersonalAccInfo(@PathVariable String name,
                                     Model model,
                                     Authentication authentication) {
        model.addAttribute("account", patronAccountService.getAccountByEmail(name));
        return "/account/accPage";
    }

    @GetMapping("account/{name}/alter")
    public String getAlterAccountPage(@PathVariable String name,
                                      Model model,
                                      Authentication authentication){
        PatronAccount patronAccount = patronAccountService.getAccountByEmail(authentication.getName());
        model.addAttribute("userForm1", patronAccount);
        return "/account/alterAcc";
    }

    @PostMapping("account/{name}/alter")
    public String postAlterAccountPage(@PathVariable String name,
                                       Authentication authentication,
                                       PatronAccount userForm1,
                                       BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/account/accPage";
        }
        patronAccountService.updateAccountInformation(authentication.getName(), userForm1);
        return "redirect:/catalog";
    }



}
