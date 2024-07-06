package io.github.qiangyt.common.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@lombok.Setter
@lombok.Getter
public abstract class AbstractAuthService implements UserDetailsService {

    @Autowired
    JwtEncoder jwtEncoder;

    @Value("${security.jwt.expiry:2592000}") // seconds. 1 month by default
    long expiry;

    
    public String currentUserId() {
        return currentUser() == null ? null : currentUser().getUsername();
    }

    public UserDetails currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        return (UserDetails)auth.getPrincipal();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var au = getAuthUser(username);
        if (au == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()                
                .username(au.getId())
                .password(/* {noop}" + */au.getPassword()).authorities("app")
                // .roles(u.getRoles())
                .build();
    }

    @lombok.Getter
    public static class AuthUser {

        final String id;

        final String password;

        public AuthUser(String id, String password) {
            this.id = id;
            this.password = password;
        }
        
    }


    protected abstract AuthUser getAuthUser(String username);

    public String generateToken(Authentication auth) {
        Instant now = Instant.now();
        var expiredAt = now.plusSeconds(getExpiry());

        var scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).expiresAt(expiredAt).subject(auth.getName())
                .claim("scope", scope).build();

        return getJwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
