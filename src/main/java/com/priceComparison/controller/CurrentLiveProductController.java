package com.priceComparison.controller;

import com.priceComparison.model.CurrentLiveProduct;
import com.priceComparison.services.CurrentLiveProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CurrentLiveProduct")
public class CurrentLiveProductController {

    @Autowired
    private CurrentLiveProductService currentLiveProductService;

    @PutMapping("/changeLiveStatus/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable(value = "id") Long id){
        String res = currentLiveProductService.changeLiveStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CurrentLiveProduct>> getAll(){
        return new ResponseEntity<>(currentLiveProductService.getCurrentLiveProducts(), HttpStatus.OK);
    }
}
