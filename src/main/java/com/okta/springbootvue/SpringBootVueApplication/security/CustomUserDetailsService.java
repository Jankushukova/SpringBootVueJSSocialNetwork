package com.okta.springbootvue.SpringBootVueApplication.security;

import com.google.common.collect.ImmutableList;
import com.okta.springbootvue.SpringBootVueApplication.models.User;
import com.okta.springbootvue.SpringBootVueApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository authenticableUsersRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository authenticableUsersRepository) {
        this.authenticableUsersRepository = authenticableUsersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authenticableUsersRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User secureUser = getUserFromUserEntity(user , Collections.EMPTY_LIST);
        return secureUser;
    }

    public User findByUsername(String username){
        return authenticableUsersRepository.findByUsername(username);
    }
    private org.springframework.security.core.userdetails.User getUserFromUserEntity(User users, List<GrantedAuthority> authorities){
        return  new org.springframework.security.core.userdetails.User(users.getUsername(), users.getPassword(),true , true , true , true, authorities);
    }
//
//    private List<GrantedAuthority> convertRolesToGrantedAuthorities(Set<Roles> roles){
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for(Roles role : roles){
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return new ArrayList<>(grantedAuthorities);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
