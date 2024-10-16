package com.lal.b_connect.auth;



import com.lal.b_connect.entity.database.UserInfo;
import com.lal.b_connect.entity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsUserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepo.findByPhoneNumber(username);
        if (user!=null) {
            return User.builder()
                    .username(user.getPhoneNumber())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRole(UserInfo userInfo) {
        if (userInfo.getRole() == null) {
            return new String[]{"USER"};
        }
        return userInfo.getRole().split(",");
    }
}
