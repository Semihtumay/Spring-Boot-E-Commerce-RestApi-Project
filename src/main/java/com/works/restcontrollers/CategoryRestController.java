package com.works.restcontrollers;

import com.works.entities.Category;
import com.works.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryRestController {
    final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Category category){
        return categoryService.add(category);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Integer id){
        return categoryService.delete(id);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return categoryService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Category category){
        return categoryService.update(category);
    }



}
