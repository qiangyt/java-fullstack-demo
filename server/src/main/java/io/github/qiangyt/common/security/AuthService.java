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
import org.springframework.stereotype.Service;

@Service
@lombok.Setter
@lombok.Getter
public class AuthService implements UserDetailsService {

    @Autowired
    JwtEncoder jwtEncoder;

    @Value("${security.jwt.expiry:2592000}") // seconds. 1 month by default
    long expiry;

    @Autowired
    SecurityMethods<?> methods;

    @SuppressWarnings("unchecked")
    public <E> AuthUser<E> currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        
        return (AuthUser<E>)getMethods().getUser(auth.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var r = getMethods().getUser(userName);
        if (r == null) {
            throw new UsernameNotFoundException("User not found with user name: " + userName);
        }
        return r;
    }

    public String generateJwt(Authentication auth) {
        // var user = currentUser();
        var now = Instant.now();
        var expiredAt = now.plusSeconds(getExpiry());

        var scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).expiresAt(expiredAt).subject(auth.getName())
                .claim("scope", scope).build();

        return getJwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
