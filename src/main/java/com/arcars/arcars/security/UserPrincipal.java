package com.arcars.arcars.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.arcars.arcars.model.User;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private User user; // Your user entity

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority>) user.getRoles(); // Return the roles of the user
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return the password of the user
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // Return the username of the user
    }

    // Implement other methods of UserDetails interface like isEnabled,
    // isAccountNonExpired, etc.
    // You may delegate these methods to your User entity.

    // Example:
    @Override
    public boolean isAccountNonExpired() {
        return true; // You may implement this based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }

    // Similarly, implement other methods...

    // You may also add custom methods specific to your application's needs.
}
