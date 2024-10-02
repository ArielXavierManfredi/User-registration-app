package com.arielxaviermanfredi.user_registration_app.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user")
@Entity(name = "tb_user")
public class User implements UserDetails  {


    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String password;

    @Column(unique = true)
    private String email;
    
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role!=null){
            return List.of(new SimpleGrantedAuthority("ROLE_"+(role.getRole().toString().toUpperCase())));
        }else{
            return null;
        }
    }

    @Override
    public String getUsername() {
        return name;
    }
}