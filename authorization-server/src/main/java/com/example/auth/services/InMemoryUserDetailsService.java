package com.example.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

///实现UserDetailsService契约，这里可以进行自定义，包括从数据库里读取
public class InMemoryUserDetailsService implements UserDetailsService {

    /// UserDetailsService将管理内存中的用户列表
    private final List<UserDetails> users;

    public InMemoryUserDetailsService(List<UserDetails> users) {
        this.users = users;

        System.out.println("++");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        ///从数据库中读取用户信息
//        try {
//            // 查询数据库用户表，获得用户信息
//            sqlSession.xxx
//            // 使用获得的信息创建SecurityUserDetails
//            SecurityUserDetails user = new SecurityUserDetails(username,
//                    password,
//                    // 以及其他org.springframework.security.core.userdetails.UserDetails接口要求的信息
//            );
//
//            logger.info("用户信息：{}", user);
//            return user;
//        } catch (Exception e) {
//            String msg = "Username: " + username + " not found";
//            logger.error(msg, e);
//            throw new UsernameNotFoundException(msg);
//        }
        ///从用户列表中筛选具有所请求用户名的用户；
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
