package com.guxian.auth;

import com.guxian.auth.entity.User;
import com.guxian.auth.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class AuthApplicationTests {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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



}
