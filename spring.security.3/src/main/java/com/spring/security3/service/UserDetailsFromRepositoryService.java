package com.spring.security3.service;

import com.spring.security3.config.ConvertUserInfoToUserDetails;
import com.spring.security3.entity.UserInfo;
import com.spring.security3.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsFromRepositoryService implements UserDetailsService {

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<UserInfo> userInfo = userInfoRepository.findByName(username);

    return userInfo.map(detail -> new ConvertUserInfoToUserDetails(detail))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
