package com.lcwd.electronic.store.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {


    private String categoryId;

    @NotBlank(message="title is required !!")
    @Size(min = 4,message = "title must be of minimum 4 characters")
    private String title;

    @NotBlank(message = "description required !")
    private String description;


    private String coverImage;

}
