package com.priceComparison.controller;

import com.priceComparison.model.Carts;
import com.priceComparison.model.Product;
import com.priceComparison.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Carts>> getAllCarts(){
        return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.OK);
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<Carts> getById(@PathVariable(value = "id") String userId){
        return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/addProductToCart/{id}")
    public ResponseEntity<Carts> addProductToCart(@PathVariable(value = "id") String cartId, @RequestBody Product product){
        return new ResponseEntity<>(cartService.addProductToCart(product, cartId), HttpStatus.OK);
    }

    @PostMapping("/createCart")
    public ResponseEntity<Carts> createCart(@RequestBody Carts cart){
        return new ResponseEntity<>(cartService.createCart(cart), HttpStatus.CREATED);
    }

}
