package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

//By using lombook
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


@Entity
@Table(name="users")
public class User  implements UserDetails {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private String userId;

    @Column(name = "user_name")
   private String name;

    @Column(name="user_password,length=10")
   private String password;

    @Column(name="user_email",unique = true)
   private String email;
   private String gender;

   @Column(length = 1000)
   private String about;

   @Column(name = "user_image_name")
   private String imageName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();



    //private Providers provider;//


    // important for roles.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        return authorities;
    }


    //important
    @Override
    public String getUsername() {
        return this.getEmail();
    }

    //important
    // lombok
    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
