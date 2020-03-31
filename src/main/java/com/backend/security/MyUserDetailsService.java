package com.backend.security;

import com.backend.user.UserEntity;
import com.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if(userEntity == null) throw new UsernameNotFoundException("Not found: User");

        CustomUser customUser = new CustomUser(userEntity.getUsername(),userEntity.getPassword(),userEntity.getRoles());
        customUser.setId(userEntity.getId());
        return customUser;
    }



}
