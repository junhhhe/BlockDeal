package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.CustomUserDetails;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberJpaRepository.findByUsername(username);

        if(memberData != null){
            return new CustomUserDetails(memberData);
        }

        return null;
    }
}
