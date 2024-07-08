package io.github.qiangyt.common.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = AuthService.class)
@Import(SecurityConfig.class)
public class AuthServiceTest {

    @Autowired
    JwtEncoder jwtEncoder;

    @Mock
    SecurityMethods<Object> securityMethods;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "notFoundUser";
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(username));
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        var username = "foundUser";
        var expected = AuthUser.simple(username, "pwd", null);
        when(securityMethods.getUser(username)).thenReturn(expected);
        
        var actual = authService.loadUserByUsername(username);
        assertSame(expected, actual);
    }

    /*@Test
    public void testGenerateToken() {
        var auth = getMockAuthentication();

        var token = authService.generateToken(auth);

        assertNotNull(token);
        Jwt jwt = Jwt.withTokenValue(token)
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(authService.getExpiry()))
                .subject("testUser")
                .claim("scope", "app")
                .build();
        assertEquals(jwt, Jwt.withTokenValue(token));
    }*/

    /*private Authentication getMockAuthentication() {
        return new Authentication() {
            @Override
            public String getName() {
                return "testUser";
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("app"));
            }

            @Override
            public Object getPrincipal() {
                return "testUser";
            }

            @Override
            public Object getCredentials() {
                return "password";
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // Nothing to do
            }

            @Override
            public Object getDetails() {
                throw new UnsupportedOperationException("Unimplemented method 'getDetails'");
            }
        };
    }*/

}