package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Product extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Detail can not be blank")
    @Length(message = "Product name must contain min 2 max  50 character.", min = 2, max = 50)
    private String name;
    @NotBlank(message = "Detail can not be blank")
    @Length(message = "Detail  must contain min 2 max  50 character.", min = 2, max = 500)
    private String detail;
    @NotNull
    @Range(message = "price can be between 0 and 99999", min = 0, max = 99999)
    private Integer price;
    private Integer stockQuantity;
    @ManyToOne
    @JoinColumn(name="categoryId",referencedColumnName = "id")
    private Category category;
    @OneToMany (mappedBy = "product",fetch =  FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Basket> baskets;
}

