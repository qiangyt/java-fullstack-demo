package com.example.demo.server.service;


import com.example.demo.server.dao.UserDao;
import com.example.demo.server.entity.UserEntity;
import com.example.demo.server.service.UserService;
import com.example.demo.sdk.req.SignUpReq;
import io.github.qiangyt.common.err.BadRequest;
import io.github.qiangyt.common.misc.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test method
    }

    // Add test methods here




    // test cases for com.example.demo.server.entity.UserEntity getByName(java.lang.String name) ---------------------

    @Test
    void testGetByNameWhenUserExists() {
        String userName = "existingUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userName);
        
        when(userDao.getByName(userName)).thenReturn(userEntity);

        UserEntity result = userService.getByName(userName);
        
        verify(userDao, times(1)).getByName(userName);
        assertNotNull(result);
        assertEquals(userName, result.getName());
    }

    @Test
    void testGetByNameWhenUserDoesNotExist() {
        String userName = "nonExistingUser";
        
        when(userDao.getByName(userName)).thenReturn(null);

        UserEntity result = userService.getByName(userName);
        
        verify(userDao, times(1)).getByName(userName);
        assertNull(result);
    }


    // test cases for void setIdGenerator(io.github.qiangyt.common.misc.IdGenerator idGenerator) ---------------------

    @Test
    void testSetIdGenerator() {
        IdGenerator newIdGenerator = mock(IdGenerator.class);
        userService.setIdGenerator(newIdGenerator);
        assertSame(newIdGenerator, userService.getIdGenerator());
    }


    // test cases for io.github.qiangyt.common.misc.IdGenerator getIdGenerator() ---------------------

    @Test
        public void testGetIdGenerator() {
            IdGenerator generator = userService.getIdGenerator();
            assertNotNull(generator);
        }


    // test cases for void setPasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) ---------------------

    @Test
    void testSetPasswordEncoder() {
        PasswordEncoder mockEncoder = mock(PasswordEncoder.class);
        userService.setPasswordEncoder(mockEncoder);
    
        assertSame(mockEncoder, userService.getPasswordEncoder());
    }


    // test cases for com.example.demo.server.entity.UserEntity signUp(com.example.demo.sdk.req.SignUpReq req) ---------------------

    
    @Test
    void testSignUpSuccess() {
        SignUpReq req = SignUpReq.builder()
            .name("testUser")
            .email("test@example.com")
            .password("P@ssw0rd")
            .build();
    
        UserEntity mappedUserEntity = new UserEntity();
        mappedUserEntity.setName(req.getName());
        mappedUserEntity.setEmail(req.getEmail());
    
        when(userDao.getByName(req.getName())).thenReturn(null);
        when(userDao.getByEmail(req.getEmail())).thenReturn(null);
        when(idGenerator.newId()).thenReturn("newId");
        when(passwordEncoder.encode(req.getPassword())).thenReturn("hashedPassword");
        when(userDao.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);
    
        UserEntity savedUser = userService.signUp(req);
    
        assertEquals("testUser", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());
        assertEquals("newId", savedUser.getId());
    }
    
    @Test
    void testSignUpUserNameDuplicates() {
        SignUpReq req = SignUpReq.builder()
            .name("duplicateUser")
            .email("test@example.com")
            .password("P@ssw0rd")
            .build();
    
        UserEntity existingUser = new UserEntity();
        when(userDao.getByName(req.getName())).thenReturn(existingUser);
    
        assertThrows(BadRequest.class, () -> userService.signUp(req));
    }
    
    @Test
    void testSignUpEmailDuplicates() {
        SignUpReq req = SignUpReq.builder()
            .name("testUser")
            .email("duplicate@example.com")
            .password("P@ssw0rd")
            .build();
    
        UserEntity existingUser = new UserEntity();
        when(userDao.getByName(req.getName())).thenReturn(null);
        when(userDao.getByEmail(req.getEmail())).thenReturn(existingUser);
    
        assertThrows(BadRequest.class, () -> userService.signUp(req));
    }


    // test cases for com.example.demo.server.dao.UserDao getDao() ---------------------

    @Test
    void testGetDao() {
        UserDao daoFromService = userService.getDao();
        assertNotNull(daoFromService);
        assertSame(userDao, daoFromService);
    }


    // test cases for org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() ---------------------

    @Test
    void testGetPasswordEncoder() {
        PasswordEncoder encoderMock = mock(PasswordEncoder.class);
        when(userService.getPasswordEncoder()).thenReturn(encoderMock);
        
        PasswordEncoder encoder = userService.getPasswordEncoder();
        
        assertNotNull(encoder);
        assertEquals(encoderMock, encoder);
    }


    // test cases for com.example.demo.server.entity.UserEntity getUser(boolean ensureExists, java.lang.String id) ---------------------

    @Test
    void testGetUser_whenUserExists() {
        String userId = "testUserId";
        UserEntity mockUser = new UserEntity();
        when(userDao.get(true, userId)).thenReturn(mockUser);

        UserEntity user = userService.getUser(true, userId);

        verify(userDao).get(true, userId);
        assertEquals(mockUser, user);
    }

    @Test
    void testGetUser_whenUserDoesNotExist() {
        String userId = "nonExistentUserId";
        when(userDao.get(false, userId)).thenReturn(null);

        UserEntity user = userService.getUser(false, userId);

        verify(userDao).get(false, userId);
        assertNull(user);
    }


    // test cases for com.example.demo.server.entity.UserEntity getByEmail(java.lang.String email) ---------------------

    @Test
        void testGetByEmail_UserExists() {
            String email = "test@example.com";
            UserEntity expectedUser = new UserEntity();
            expectedUser.setEmail(email);
            
            when(userDao.getByEmail(email)).thenReturn(expectedUser);
    
            UserEntity actualUser = userService.getByEmail(email);
    
            verify(userDao).getByEmail(email);
            assertEquals(expectedUser, actualUser);
        }
    
        @Test
        void testGetByEmail_UserDoesNotExist() {
            String email = "nonexistent@example.com";
            
            when(userDao.getByEmail(email)).thenReturn(null);
    
            UserEntity actualUser = userService.getByEmail(email);
    
            verify(userDao).getByEmail(email);
            assertNull(actualUser);
        }


    // test cases for void setDao(com.example.demo.server.dao.UserDao dao) ---------------------

    @Test
    void testSetDao() {
        UserDao newDao = mock(UserDao.class);
        userService.setDao(newDao);
        assertSame(newDao, userService.getDao());
    }

}