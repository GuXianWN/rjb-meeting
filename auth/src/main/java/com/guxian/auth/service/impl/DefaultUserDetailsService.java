package com.guxian.auth.service.impl;

import com.guxian.auth.service.UserService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Setter(onMethod_ = @Autowired)
@Service("userDetailsService")
@Log4j2
public class DefaultUserDetailsService implements UserDetailsService {

    PasswordEncoder passwordEncoder;
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByUsernameLimitOne(username)
                .orElseThrow(() -> {
                    log.info("User not found: " + username);
                    throw new UsernameNotFoundException("用户不存在");
                });
    }
}
