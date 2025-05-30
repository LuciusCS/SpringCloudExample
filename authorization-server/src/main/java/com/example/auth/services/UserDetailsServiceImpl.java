package com.example.auth.services;

import com.example.auth.bean.User;
import com.example.auth.oauth2.client.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user; // 直接返回 User 实体（已实现 UserDetails）
    }

    public boolean save(User user) {
        User user1= userRepository.save(user);
        if(user1!=null) {
            return  true;
        }else{
            return  false;
        }
    }
}