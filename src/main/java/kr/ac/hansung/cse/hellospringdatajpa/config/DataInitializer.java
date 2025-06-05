package kr.ac.hansung.cse.hellospringdatajpa.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import kr.ac.hansung.cse.hellospringdatajpa.entity.Member;
import kr.ac.hansung.cse.hellospringdatajpa.repo.MemberRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByEmail("admin@example.com").isEmpty()) {
            Member admin = new Member();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setRole("ROLE_ADMIN");
            memberRepository.save(admin);
            System.out.println("Admin account created.");
        }
    }
}
