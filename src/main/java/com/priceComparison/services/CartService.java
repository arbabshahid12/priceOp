package com.priceComparison.services;

import com.priceComparison.model.Carts;
import com.priceComparison.model.Product;
import com.priceComparison.repositories.CartRepositoryy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepositoryy cartRepository;

    public List<Carts> getAllCarts(){
        return cartRepository.findAll();
    }

    public Carts getCartByUserId(String userId){
        List<Carts> carts = cartRepository.findAll();
        Carts userCart = null;
        for(Carts cart:carts){
            if(cart.getUser().getId().equals(userId)){
                userCart = cart;
            }else {
                System.out.println("User not found");
            }
        }
        return userCart;
    }

    public Carts addProductToCart(Product product, String cartId){
        List<Carts> carts = cartRepository.findAll();
        Carts userCart = null;
        for(Carts cart: carts){
            if(cart.getId().equals(cartId)){
                List<Product> products = cart.getProducts();
                products.add(product);
                userCart = cart;
            }else {
                System.out.println("Cart not foound");
            }
        }
        return userCart;
    }

    public Carts createCart(Carts cart){
        cart.setUser(null);
        cart.setProducts(null);
        return cartRepository.save(cart);

    }
}
