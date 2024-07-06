package io.github.qiangyt.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@lombok.Getter
public class AuthUser<E> implements UserDetails {

    final UserDetails target;

    final E extra;

    public AuthUser(UserDetails target, E extra) {
        this.target = target;
        this.extra = extra;
    }

    public static <E> AuthUser<E> simple(String userName, String password, E extra) {
        var target = User.builder().username(userName).password(/* {noop}" + */password).authorities("app")
                // .roles(u.getRoles())
                .build();
        return new AuthUser<>(target, extra);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getTarget().getAuthorities();
    }

    public String getPassword() {
        return getTarget().getPassword();
    }

    public String getUsername() {
        return getTarget().getUsername();
    }

    public boolean isAccountNonExpired() {
        return getTarget().isAccountNonExpired();
    }

    public boolean isAccountNonLocked() {
        return getTarget().isAccountNonLocked();
    }

    public boolean isCredentialsNonExpired() {
        return getTarget().isCredentialsNonExpired();
    }

    public boolean isEnabled() {
        return getTarget().isEnabled();
    }

}