package com.example.boardpractice.config.auth;

import com.example.boardpractice.entity.Users;
import com.example.boardpractice.exception.NotFoundException;
import com.example.boardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Users user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("이메일이 일치하지 않습니다."));

        return new PrincipalDetails(user);
    }
}
