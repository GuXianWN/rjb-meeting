package com.guxian.auth.service.impl;

import com.guxian.auth.entity.User;
import com.guxian.auth.entity.UserStatus;
import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.entity.vo.RegisterVo;
import com.guxian.auth.service.UserService;
import com.guxian.common.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private PasswordEncoder mockPasswordEncoder;

    private UserServiceImpl userServiceImplUnderTest;


    @BeforeEach
    void setUp() {
        userServiceImplUnderTest = spy(mock(UserServiceImpl.class, Mockito.CALLS_REAL_METHODS));
        mockPasswordEncoder = mock(PasswordEncoder.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    void testFindByUsernameLimitOne() {
        // Setup
        final User user = new User();
        user.setRoleId(RoleType.ROLE_USER);
        user.setId(0L);
        user.setUsername("userName");
        user.setPassword("password");
        user.setAccount("account");
        user.setEmail("email");
        user.setPortraitUrl("portraitUrl");
        user.setMobile("mobile");
        user.setStatus(UserStatus.NORMAL);
        user.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Optional<User> expectedResult = Optional.of(user);

        // Run the test
        final Optional<User> result = userServiceImplUnderTest.findByUsernameLimitOne("account");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testLogin() {
        // Setup
        final LoginVo loginVo = new LoginVo();
        loginVo.setUsername("username");
        loginVo.setPassword("password");
        loginVo.setCaptcha("captcha");

        final HttpServletRequest request = new MockHttpServletRequest();
        when(mockPasswordEncoder.matches("password", "password")).thenReturn(false);

        // Run the test
        final String result = userServiceImplUnderTest.login(loginVo, request);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testLogout() {
        // Setup
        final HttpServletRequest request = new MockHttpServletRequest();

        // Run the test
        userServiceImplUnderTest.logout(request);

        // Verify the results
    }

    @Test
    void testRegister() {
        // Setup
        final RegisterVo user = new RegisterVo();
        user.setUsername("userName");
        user.setPassword("password");
        user.setAccount("account");

        final User user1 = new User();
        user1.setRoleId(RoleType.ROLE_USER);
        user1.setUsername("userName");
        user1.setPassword("password");
        user1.setAccount("account");
        user1.setEmail("email");
        user1.setPortraitUrl("portraitUrl");
        user1.setMobile("mobile");
        user1.setStatus(UserStatus.NORMAL);
        user1.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final Optional<User> expectedResult = Optional.of(user1);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        // Run the test
        final Optional<User> result = userServiceImplUnderTest.register(user);

        // Verify the results
        assertThat(result.get().getId()).isNotNull();
    }
}
