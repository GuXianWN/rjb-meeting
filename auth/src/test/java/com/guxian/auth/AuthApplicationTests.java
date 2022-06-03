package com.guxian.auth;

import com.guxian.auth.config.InitAuth;
import com.guxian.auth.entity.User;
import com.guxian.auth.service.UserService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class AuthApplicationTests {

    @Autowired
    private InitAuth initAuth;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void authConfigureTest() {
        assertNotNull(initAuth.getSecret(), "secret is null");  // JUnit assertion
    }

    @Test
    @Commit
    @Transactional
    void addUserTest() {
        final Long id = 17701L;
        userService.removeById(id);

        userService.save(
                new User()
                        .setId(id)
                        .setUsername("root")
                        .setPassword(passwordEncoder.encode("root")));

        assertNotNull(userService.getById(17701L), "user is null");
    }


    @Test
    void secretValueTest() {
        log.info("encoding secret: {}", passwordEncoder.encode(initAuth.getSecret()));
    }
}
