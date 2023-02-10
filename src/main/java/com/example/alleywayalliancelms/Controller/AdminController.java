package com.example.alleywayalliancelms.Controller;

import com.example.alleywayalliancelms.Service.PatronAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private PatronAccountService patronAccountService;


    @GetMapping("admin")
    public String showAllUsers(Model model) {
        model.addAttribute("userList", patronAccountService.getAllAccountsWithSortedCheckouts());
        return "/admin/adminPage";
    }

    @PostMapping("admin")
    public String deleteUser(@RequestParam(required = true, defaultValue = "") String userId,
                             @RequestParam(required = true, defaultValue = "") String action,
                             Model model) {

        if (action.equals("delete")) {
            patronAccountService.deletePatronAccount(userId);
        }

        return "redirect:/admin";
    }

    @GetMapping("admin/get/{userId}")
    public String getUser(@PathVariable("userId") String userId, Model model) {
        model.addAttribute("allUsers", patronAccountService.findPatronAccountById(userId));
        return "/admin/adminPage";
    }

}
