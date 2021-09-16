package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    // @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String homeLogin(
            @CookieValue(name="memberId", required = false) Long memberId, Model model){
        // required = false 하는 이유 : 로그인 안 한(쿠키없는)사용자도 들어와야 하니까.
        if (memberId == null){
            return "home";
        }

        // 쿠키가 DB에 있는 회원PK와 같은지 확인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        // 쿠키가 DB에 있는 회원PK와 같다고 확인 성공
        model.addAttribute("member", loginMember);
        return "loginHome";
    }


}