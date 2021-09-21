package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    // @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member loginMember, Model model){

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 loginHome 으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /**
     * 실무에서 세션 객체는 Member 객체 전부 넣으면 안된다.
     * 최소한의 데이터만 넣어야 한다.
     * 왜냐하면 로그인한 사용자가 많아질수록 '객체 용량 * 사용자수' 만큼의 서버 메모리를 소모하기 때문이다.
     * 로그인 용 멤버객체를 따로 만들자.
     * 아이디나 회원명 정도 최소한의 개수로 데이터를 담자.
     * */

    // @GetMapping("/")
    public String homeLoginV1(
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
    
    // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if(member == null){
            return "home";
        }

        // 쿠키가 DB에 있는 회원PK와 같다고 확인 성공
        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        // 세션을 찾아서 사용하는 시점에는 create:false 옵션을 사용하여 세션을 생성하지 말아야 한다.
        // 그저 조회하고 싶기 때문이다.
        if (session == null) {
            return "home"; // 세션이 없으면 home
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 loginHome 으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}