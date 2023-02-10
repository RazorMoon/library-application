package com.example.alleywayalliancelms.RestControllers;

import com.example.alleywayalliancelms.Model.Hold;
import com.example.alleywayalliancelms.Service.HoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class HoldRestController {


    @Autowired
    private HoldService holdService;

    @GetMapping("holds")
    public List<Hold> getAllHolds() {
        return holdService.getAllHoldsList();
    }

    @PostMapping("holds/add")
    public ResponseEntity<?> addHold(@RequestBody Hold hold) {
        return new ResponseEntity<>(holdService.save(hold), HttpStatus.CREATED);
    }
}
