package com.works.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Category extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    @Length(message = "Category name cannot exceed 50 characters.", min = 0, max = 50)
    @NotBlank(message = "Please Enter Category Name")
    private String categoryName;
}
