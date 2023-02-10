package com.example.alleywayalliancelms.Controller;

import com.example.alleywayalliancelms.Service.HoldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class HoldController {

    @Autowired
    public HoldService holdService;


    @GetMapping("hold")
    public String getAccountHoldListPage(Model model,
                                         Authentication authentication) {
        model.addAttribute("holdList", holdService.getHoldsByPatronEmail(authentication.getName()));
        return "hold/holdPage";
    }

    @GetMapping("hold/active")
    public String getActiveHoldListPage(Model model,
                                        Authentication authentication) {
        model.addAttribute("activeHoldList",
                holdService.getActiveHoldList(holdService.getHoldsByPatronEmail(authentication.getName())));
        log.info("activeHoldList : {}", holdService.getActiveHoldList(holdService.getHoldsByPatronEmail(authentication.getName())));
        return "hold/activePage";
    }

    @GetMapping("hold/expired")
    public String getExpiredHoldListPage(Model model,
                                         Authentication authentication) {
        model.addAttribute("expiredHoldList",
                holdService.getExpiredHoldList((holdService.getHoldsByPatronEmail(authentication.getName()))));
        return "hold/expiredPage";
    }


}
