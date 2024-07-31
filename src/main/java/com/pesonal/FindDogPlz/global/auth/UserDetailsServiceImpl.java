package com.pesonal.FindDogPlz.global.auth;

import com.pesonal.FindDogPlz.domain.Member;
import com.pesonal.FindDogPlz.domain.MemberAdapter;
import com.pesonal.FindDogPlz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디"));
        return new MemberAdapter(member);
    }
}
