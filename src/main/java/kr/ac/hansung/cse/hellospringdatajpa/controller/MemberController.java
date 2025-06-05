package kr.ac.hansung.cse.hellospringdatajpa.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.hellospringdatajpa.entity.Member;
import kr.ac.hansung.cse.hellospringdatajpa.repo.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberController(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member()); // 바인딩할 객체를 모델에 추가
        return "register"; // register.html 템플릿
    }


    @PostMapping("/register")
    public String registerMember(@Valid @ModelAttribute("member") Member member,
                                 BindingResult bindingResult,
                                 Model model) {

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(member.getEmail())) {
            bindingResult.rejectValue("email", "error.member", "이미 사용 중인 이메일입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "register"; // 에러 있으면 다시 폼으로
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole("ROLE_USER");
        memberRepository.save(member);
        return "redirect:/login?success"; // 성공 시 리다이렉트
    }




    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
        if (logout != null) model.addAttribute("logoutMsg", "로그아웃되었습니다.");
        return "login";
    }
    @GetMapping("/user_list")
    public String userList(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "user_list";  // user_list.html 뷰 이름
    }

}
