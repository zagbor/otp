package com.zagbor.otp.service;

import com.zagbor.otp.entity.User;
import com.zagbor.otp.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User getByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getEncryptedPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
