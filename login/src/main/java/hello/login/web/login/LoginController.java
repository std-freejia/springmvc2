package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindResult,
                        HttpServletResponse response){
        /** 샘플 계정 ID: test , PW: test!   */
        if (bindResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // 아이디 또는 패스워드가 안맞는 경우. reject() global 오류
        if(loginMember == null){
            bindResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        /**  로그인 성공 처리 : response 에 쿠키 보냄 */
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        // 회원PK 를 String으로 변환하여 보냄

        response.addCookie(idCookie);  // 시간 정보 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)

        return "redirect:/";
    }

    // 로그아웃: 쿠키의 시간을 없앤다.
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);    // 만료 시간 0 설정.
        response.addCookie(cookie);
    }
}
