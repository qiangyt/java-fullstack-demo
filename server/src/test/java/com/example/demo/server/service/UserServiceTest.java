
package com.example.demo.server.service;


import io.github.qiangyt.common.err.BadRequest;
import io.github.qiangyt.common.misc.IdGenerator;
import com.example.demo.sdk.ErrorCode;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.dao.UserDao;
import com.example.demo.server.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.lang.reflect.Field;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao dao;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ????????????




    // test cases for com.example.demo.server.entity.UserEntity getByName(java.lang.String name) ---------------------

    @Test
    void testGetByNameWhenUserExists() {
        String name = "user123";
        UserEntity user = new UserEntity();
        when(dao.getByName(name)).thenReturn(user);
            
        UserEntity result = userService.getByName(name);
            
        assertEquals(user, result);
    }
    
    @Test
    void testGetByNameWhenUserDoesNotExist() {
        String name = "nonexistentUser";
        when(dao.getByName(name)).thenReturn(null);
    
        assertThrows(BadRequest.class, () -> userService.getByName(name));
    }
    
    @Test
    void testGetByNameWithNullName() {
        String name = null;
        doNothing().when(dao).getByName(any());
        
       assertNull(userService.getByName(name));
    }
    
    @Test
    void testGetByNameWithEmptyString() {
        String name = "";
        UserEntity result = userService.getByName(name);
            
       assertNull(result);
    }


    // test cases for void <init>() ---------------------

    
    @Test
    void testSignUp_ExistingUser() {
        // Arrange
        String name = "testuser";
        String email = "test@example.com";
        String password = "password123!";
        
        UserEntity existingUser = new UserEntity();
        when(dao.getByName(name)).thenReturn(existingUser);
        
        // Act & Assert
        assertThrows(BadRequest.class, () -> {
            userService.signUp(name, email, password);
        });
    }
    
    @Test
    void testSignUp_NewUser() throws Exception{
        // Arrange
        String name = "newuser";
        String email = "new@example.com";
        String password = "password123!";
        
        when(dao.getByEmail(email)).thenReturn(null);
        when(dao.getByName(name)).thenReturn(null);
        when(idGenerator.newId()).thenReturn("TESTID");
        when(passwordEncoder.encode(anyString())).thenReturn("ENCRYPTED_PASSWORD");
        
        // Act
        UserEntity result = userService.signUp(name, email, password);
        
        // Assert
        verify(dao).create(result);
        
        assertEquals("ENCRYPTED_PASSWORD", result.getPassword());
        assertEquals("TESTID", result.getId());
    }


    // test cases for void setIdGenerator(io.github.qiangyt.common.misc.IdGenerator idGenerator) ---------------------



    private IdGenerator mockId1, mockId2;
    private UserDao userDao = Mockito.mock(UserDao.class);
    private UserService userService;

    @BeforeEach
    public void setUp() {
       .userService = new UserService(userDao());
        mockId1 = Mockito.mock(IdGenerator.class);
        mockId2 = Mockitomock(IdGenerator.id.class);
    }

    @Test
    public void testSetIdGeneratorWithFirstGenerator() throws Exception {
        // Setter up the first ID generator
        when(mockId1.newId()).thenReturn("test-id-1");
        userService.setIdGenerator(mockId1);

        // Perform signup
        UserEntity user = new UserEntity();
        user.setUsername("test-user");
        userService.signup(user);
        
        // Verify the saved user has the correct ID
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userDao).saveUser(captor.capture());
        assertEquals("test-id-1", captor.getValue().getId());
    }

    @Test
    public void testSetIdGeneratorWithSecondGenerator() throws Exception {
        // Setup the second ID generator
        when(mockId2.newId()).thenReturn("test-id-2");
        userService.setIdGenerator(mockId2);

        // Perform signup
        UserEntity user = new UserEntity();
        user.setUsername("another-test-user");
        userService.signup(user);
        
        // Verify the saved user has the correct ID from the second generator
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userDao).saveUser(captor.capture());
        assertEquals("test-id-2", captor.getValue().getId());
    }


    // test cases for io.github.qiangyt.common.misc.IdGenerator getIdGenerator() ---------------------

    @Test
    public void testGetIdGenerator() {
        assertEquals(idGenerator, userService.getIdGenerator());
    }


    // test cases for void setPasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) ---------------------
    
    // Add this method
    @Test
    void setPasswordEncoder_ShouldSetThePasswordEncoder() {
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        userService.setPasswordEncoder(encoder);
    }
    
    // Existing code remains unchanged


    // test cases for com.example.demo.server.entity.UserEntity signUp(com.example.demo.sdk.req.SignUpReq req) ---------------------

    @Test
    public void testSignUpNormal() {
        // Arrange
        String expectedUsername = "testUser";
        String expectedPassword = "Test@123456";
        String expectedEmail = "test@mail.com";
    
        SignUpReq req = new SignUpReq();
        req.setName(expectedUsername);
        req.setPassword(expectedPassword);
        req.setEmail(expectedEmail);
    
        when(dao.getUser(any())).thenReturn(null);
        
        // Assume the generated id
        String generatedId = idGenerator.newId();
        when(idGenerator.newId()).thenReturn(generatedId);
    
        when(passwordEncoder.encode(expectedPassword)).thenReturn("encoded_password");
    
        // Act
        UserEntity result = userService.signUp(req);
    
        // Assert
        assertNotNull(result);
        assertEquals(expectedUsername, result.getUsername());
        assertEquals(expectedEmail, result.getEmail());
        assertEquals(generatedId, result.getId());
        verify(dao).save(any(UserEntity.class));
    }
    
    @Test
    public void testSignUpDuplicateName() {
        // Arrange
       SignUpReq req = new SignUpReq();
        req.setName("existingUser");
        
        when(dao.getUser(req.getName())).thenReturn(new UserEntity());
    
        // Act and Assert
        assertThrows(BadRequest.class, () -> userService.signUp(req));
    }
    
    @Test
    public void testSignUpInvalidRequest() {
        // Arrange
        SignUpReq invalidReq = new SignUpReq();
        
        // Act and Assert
        assertThrows(BadRequest.class, () -> userService.signUp(invalidReq));
    }


    // test cases for com.example.demo.server.dao.UserDao getDao() ---------------------

    @Test
    void testGetDao_ShouldReturnUser_WhenUserExists() {
        UserEntity user = new UserEntity();
        when(dao.get(any(String.class))).thenReturn(user);
        
        UserEntity result = userService.getUser("some-id", false);
        assertEquals(user, result);
    }
    
    @Test
    void testGetDao_ShouldThrowException_WhenUserDoesNotExistAndEnsureExistsTrue() {
        when(dao.get(any(String.class))).thenReturn(null);
        
        assertThrows(BusinessException.class, () -> {
            userService.getUser("non-existent-id", true);
        });
    }


    // test cases for org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() ---------------------

    @Test
    void usernameExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existing");
        
        when(dao.findByUsername(any())).thenReturn(existingUser);
        
        try {
            userService.signUp(new SignUpReq());
            fail("Expected exception not thrown");
        } catch (BadRequest e) {
            verify(dao).findByUsername(any());
            assertEquals(ErrorCode.USERNAME_EXISTS, e.getCode());
        }
    }
    
    @Test
    void emailExists() {
        UserEntity existingEmail = new UserEntity();
        existingEmail.setEmail("existing@example.com");
        
        when(dao.findByEmail(any())).thenReturn(existingEmail);
        
        try {
            userService.signUp(new SignUpReq());
            fail("Expected exception not thrown");
        } catch (BadRequest e) {
            verify(dao).findByEmail(any());
            assertEquals(ErrorCode.EMAIL_EXISTS, e.getCode());
        }
    }
    
    @Test
    void successfulRegistration() {
        when(dao.findByUsername(any())).thenReturn(null);
        when(dao.findByEmail(any())).thenReturn(null);
    
        String encoded = "encoded";
        when(passwordEncoder.encode(any())).thenReturn(encoded);
        
        UserEntity savedUser = new UserEntity();
        when(dao.saveAndFlush(any())).thenReturn(savedUser);
    
        SignUpReq signUpReq = new SignUpReq();
        signUpReq.setName("username");
        signUpReq.setPassword("password");
        signUpReq.setEmail("email@example.com");
    
        String result = userService.signUp(signUpReq);
        
        verify(passwordEncoder).encode(any());
        assertThat(result).isEqualTo("username");
    }


    // test cases for com.example.demo.server.entity.UserEntity getUser(boolean ensureExists, java.lang.String id) ---------------------





    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case 1: User exists and ensureExists is true
    @Test
    void getUser_WhenUserExistsAndEnsureExistsIsTrue_ShouldReturnUser() {
        String id = "test-user-id";
        UserEntity mockUser = new UserEntity();
        when(userDao.findById(anyString())).thenReturn(Optional.of(mockUser));
        
        ResponseEntity<User> result = userService.getUser(true, id);
        
        assert result.getStatusCode().equals(HttpStatus.OK);
    }

    // Test case 2: User exists and ensureExists is false
    @Test
    void getUser_WhenUserExistsAndEnsureExistsIsFalse_ShouldReturnUser() {
        String id = "test-user-id";
        UserEntity mockUser = new UserEntity();
        when(userDao.findById(anyString())).thenReturn(Optional.of(mockUser));
        
        ResponseEntity<User> result = userService.getUser(false, id);
        
        assert result.getStatusCode().equals(HttpStatus.OK);
    }

    // Test case 3: User does not exist and ensureExists is true
    @Test
    void getUser_WhenUserDoesNotExistAndEnsureExistsIsTrue_ShouldThrowBadRequest() {
        String id = "nonexistent-id";
        when(userDao.findById(anyString())).thenReturn(Optional.empty());
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> userService.getUser(true, id)
        );
        
        // Assert the status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode().value());
        // Verify the exception message includes expected error code
        assertTrue(exception.getMessage().contains("user.not-found"));
    }

    // Test case 4: User does not exist and ensureExists is false (Optional)
    @Test
    void getUser_WhenUserDoesNotExistAndEnsureExistsIsFalse_ShouldReturnNullOrAppropriateResponse() {
        String id = "nonexistent-id";
        when(userDao.findById(anyString())).thenReturn(Optional.empty());
        
        ResponseEntity<User> result = userService.getUser(false, id);
        
        // Depending on implementation, assert if the return is null or appropriate status
        assertEquals(HttpStatus.OK.getStatusCode(), result.getStatusCode().value());
        assertNull(result.getBody());
    }


    // test cases for com.example.demo.server.entity.UserEntity getByEmail(java.lang.String email) ---------------------

    @Test
    public void getByEmail_WhenUserExists_ReturnsUserEntity() {
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(email);
        
        when(dao.getByEmail(email)).thenReturn(expectedUser);
        
        UserEntity result = userService.getByEmail(email);
        
        assertEquals(expectedUser, result);
    }
    
    @Test
    public void getByEmail_WhenUserDoesNotExist_ReturnsNull() {
        String email = "nonexistent@example.com";
        
        when(dao.getByEmail(email)).thenReturn(null);
        
        UserEntity result = userService.getByEmail(email);
        
        assertNull(result);
    }


    // test cases for void setDao(com.example.demo.server.dao.UserDao dao) ---------------------




@RunWith(JUnit4.class)

    @Mock
    UserDao userDao;

    @InjectMocks
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Cleanup if necessary
    }

    @Test
    public void testSetDao() {
        try {
            // Create a new UserDao mock
            UserDao newDao = any(UserDao.class);

            // Set the newDao on userService
            userService.setDao(newDao);

            // Access the private 'dao' field in UserService
            Field field = UserService.class.getDeclaredField("dao");
            field.setAccessible(true);
            UserDao currentDao = (UserDao) field.get(userService);

            // Verify that the internal dao reference is updated to newDao
            assertEquals(newDao, currentDao);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Unexpected exception while accessing DAO: " + e.getMessage());
        }
    }