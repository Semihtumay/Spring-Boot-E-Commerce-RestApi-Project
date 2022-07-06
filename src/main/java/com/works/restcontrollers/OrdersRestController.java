package com.works.restcontrollers;

import com.works.entities.Orders;
import com.works.services.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrdersRestController {

    final OrdersService ordersService;

    public OrdersRestController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
    @PostMapping("/add")
    public ResponseEntity add(){
        return ordersService.add();
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam long id){

        return ordersService.delete(id);
    }

    @GetMapping("/getDetail")
    public ResponseEntity getDetail(long id){
            return ordersService.getOrderByOrder_Id(id);
        }

    @GetMapping("/list")
    public ResponseEntity list(){

        return ordersService.list();
    }

    @GetMapping("/customer")
    public ResponseEntity getOrderByCustomer(){

        return ordersService.getOrderByCustomer();
    }
}


