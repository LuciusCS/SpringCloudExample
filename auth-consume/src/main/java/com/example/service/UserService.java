package com.example.service;

import com.example.entity.AuthRole;
import com.example.entity.AuthUser;
import com.example.entity.AuthUserRole;
import com.example.entity.dto.UserRegisterRequestDTO;
import com.example.repository.UserRepository;
import com.example.repository.UserRoleRepository;
import com.example.security.SecurityPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleService roleService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUser authUser= userRepository.findByUserName(username);

        if(authUser!= null){
            List<AuthUserRole> userRoles=userRoleRepository.findAllByUserId(authUser.getId());

            Collection<SimpleGrantedAuthority>authorities=new ArrayList<>();

            userRoles.forEach(userRole -> {
                authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
            });

            UserDetails principal= new User(authUser.getUsername(),authUser.getPassword(),authorities);

            return  principal;

        }

        return null;
    }


    public  AuthUser findByUserName(String userName){
        return  userRepository.findByUserName(userName);
    }


    public String createUser(UserRegisterRequestDTO requestDTO){

        try{
            AuthUser user=(AuthUser) dtoMapperRequestDtoToUser(requestDTO);

            user=userRepository.save(user);

            if(!requestDTO.getRoleList().isEmpty()) {
                for(String role: requestDTO.getRoleList()){
                    AuthRole existingRole=roleService.findRoleByName("ROLE_"+role.toUpperCase());
                    if(existingRole!=null){
                        addUserRole(user,existingRole);
                    }
                }
            }

            return "User successfully created .";

        }catch (Exception e){
            e.printStackTrace();;
            return e.getCause().getMessage();
        }

    }

    public List<AuthUser>retriveAllUserList(){
        return  userRepository.findAll();
    }


     public  AuthUser updateAuthUser(UserRegisterRequestDTO userRegisterRequestDTO){
        AuthUser authUser=(AuthUser) dtoMapperRequestDtoToUser(userRegisterRequestDTO);

        authUser=userRepository.save(authUser);

        addUserRole(authUser,null);
        return authUser;

     }


    public AuthUser findCurrentUser(){
        return userRepository.findById(SecurityPrincipal.getInstance().getLoggedInPrincipal().getId()).get();
    }


    public List<AuthUserRole>findAllCurrentUserRole(){
        return  userRoleRepository.findAllByUserId(SecurityPrincipal.getInstance().getLoggedInPrincipal().getId());
    }


    public Optional<AuthUser> findUserById(long id){
        return  userRepository.findById(id);
    }


    ///用于给用户添加角色
    public  void  addUserRole(AuthUser user, AuthRole authRole){
        AuthUserRole authUserRole=new AuthUserRole();
        authUserRole.setUser(user);

        if(authRole==null){
            authRole=roleService.findDefaultRole();
        }

        authUserRole.setRole(authRole);

        userRoleRepository.save(authUserRole);
    }


    ///用于将dto转换为user
    private  Object dtoMapperRequestDtoToUser(UserRegisterRequestDTO source){
        AuthUser authUser=new AuthUser();
        authUser.setUserName(source.getUsername());
        authUser.setPassword(source.getPassword());
        authUser.setEntityNo(source.getEntityNo());

        return  authUser;

    }



}
