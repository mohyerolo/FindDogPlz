package com.pesonal.FindDogPlz.member.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberAdapter extends User {

    private final Member member;

    public MemberAdapter(final Member member) {
        super(member.getLoginId(), member.getPassword(), authorities(member.getRoles()));
        this.member = member;
    }

    private static Collection<? extends GrantedAuthority> authorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
