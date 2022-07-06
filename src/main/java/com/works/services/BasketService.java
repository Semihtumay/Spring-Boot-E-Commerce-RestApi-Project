package com.works.services;

import com.works.entities.Basket;
import com.works.entities.Customer;
import com.works.entities.Product;
import com.works.repositories.BasketRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class BasketService {


    final BasketRepository basketRepo;
    final ProductRepository productRepository;
    final HttpSession session;


    public BasketService(BasketRepository basketRepository, ProductRepository productRepository, HttpSession session) {
        this.basketRepo = basketRepository;

        this.productRepository = productRepository;
        this.session = session;
    }

    public ResponseEntity add(Basket basket) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
      Customer customer = (Customer) session.getAttribute("customer");
      List<Basket> baskets=basketRepo.findByCreatedByEqualsAndStatusFalse(customer.getEmail());
        boolean isSameProduct=false;
        Long basketId= Long.valueOf(0);
        int oldQuantityBasket=0;
        Optional<Product> optionalProduct = productRepository.findById(basket.getProduct().getId());
        if (optionalProduct.isPresent()) {
            for(Basket basketItem:baskets) {
                if (basketItem.getProduct().getId() == basket.getProduct().getId()) {
                    isSameProduct=true;
                    basketId=basketItem.getId();
                    oldQuantityBasket=basketItem.getQuantity();
                    break;
                }
            }
            Product product=optionalProduct.get();
            Integer stockQuantity = product.getStockQuantity();
            Integer basketQuantity= basket.getQuantity();

            if (basketQuantity <= stockQuantity) {
                product.setStockQuantity(stockQuantity-basketQuantity);
                productRepository.save(product);
                basket.setProduct(product);
                if(isSameProduct){
                    basket.setId(basketId);
                    basket.setQuantity(basketQuantity+oldQuantityBasket);
                    basket.setCreatedBy(customer.getEmail());
                }
                basketRepo.save(basket);
                hm.put(REnum.status, true);
                hm.put(REnum.result, basket);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Not enough stock");
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }


        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "There is not such a product");
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }

    }

    public ResponseEntity delete(Long id) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
            Optional<Basket> optionalBasket = basketRepo.findById(id);
            if (optionalBasket.isPresent()) {
                basketRepo.deleteById(id);
                Product product = optionalBasket.get().getProduct();
                product.setStockQuantity(product.getStockQuantity() + optionalBasket.get().getQuantity());
                productRepository.save(product);
                hm.put(REnum.status, true);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not such a basket");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
    }

    public ResponseEntity update(Long id,Integer quantity) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Optional<Basket> optionalBasket = basketRepo.findById(id);
            if (optionalBasket.isPresent()) {
                Basket oldBasket=optionalBasket.get();
                int oldBasketQuantity=oldBasket.getQuantity();
                Integer difference=oldBasketQuantity-quantity;
                oldBasket.setQuantity(quantity);
                basketRepo.saveAndFlush(oldBasket);
                Product product=oldBasket.getProduct();
                if(oldBasketQuantity>quantity){

                    product.setStockQuantity(product.getStockQuantity()+difference);
                }else{

                    product.setStockQuantity(product.getStockQuantity()-difference);
                }
                productRepository.save(product);

                hm.put(REnum.status, true);
                hm.put(REnum.message, "Update is successful");
                System.out.println("if" + hm);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not such a basket");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.message, ex.getMessage());

        }

        return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getBasketList_by_Customer(String email){
        Map<REnum, Object> hm = new HashMap<>();
        List<Basket> baskets=basketRepo.findByCreatedByEqualsIgnoreCase(email);
        hm.put(REnum.status, true);
        hm.put(REnum.result, baskets);
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }


}



