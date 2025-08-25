package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.User;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    UserDto user;
    //    private String jwtToken;
    private String refreshToken;
//    private RefreshTokenDto refreshToken;


}
