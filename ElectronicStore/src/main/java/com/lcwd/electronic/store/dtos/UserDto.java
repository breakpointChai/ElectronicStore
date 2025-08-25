package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {


    private String userId;

    @Size(min = 2, max = 20,message = "Invalid Name")
    private String name;

    @NotBlank(message = "Password Required")
    private String password;

    //for strong email check we use regular expression
//    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\\\.[a-z]{2,3}",message = "Invalid user email")
    @Email(message = "Invalidate Email !!!")
    @NotBlank(message = "Email is Required")
    private String email;

    @Size(min = 4, max = 6,message = "Invalid gender")
    private String gender;

    @NotBlank(message = "write something about yourself")
    private String about;

    @ImageNameValid
    private String imageName;


    private List<RoleDto> roles;
    //@Pattern
    //for custom validator


//    private Providers provider;

}
