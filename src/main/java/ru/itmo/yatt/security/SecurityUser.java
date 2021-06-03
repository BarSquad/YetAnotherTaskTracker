package ru.itmo.yatt.security;

import lombok.Data;
import ru.itmo.yatt.model.Role;
import ru.itmo.yatt.model.UserStatus;
import ru.itmo.yatt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, Set<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new SecurityUser(
                user.getEmail(), user.getPassword(),
                user.getRole().getAuthorities(), user.getStatus().equals(UserStatus.ACTIVE)
        );
    }

    public boolean hasAuthority(String authority) {
        return this.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.toString().equals(authority));
    }

    public boolean hasRole(String role) {
        return hasAuthority("ROLE_" + role);
    }

    public boolean hasRole(Role role) {
        return hasAuthority("ROLE_" + role);
    }
}
