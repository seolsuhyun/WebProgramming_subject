package kr.ac.hansung.cse.hellospringdatajpa.service;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Member;
import kr.ac.hansung.cse.hellospringdatajpa.repo.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Member member) {
        if (memberRepository.findByEmail(member .getEmail()).isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole("ROLE_USER");  // 기본 권한 지정
        memberRepository.save(member);
    }
}
