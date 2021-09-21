package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    /** V3: 서블릿 HTTP 세션1 */
    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("login Fail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        /* 로그인 성공 처리
         request.getSession(): 세션이 있으면 있는 세션 반환. 2가지 create 옵션이 있다.
          - true(default): 없으면 신규 세션 생성
          - false: 없어도 신규 세션 생성하지 않음. 없으니까 null 반환.  */
        HttpSession session = request.getSession(); // true
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    /** V2 : 직접 만든 세션 적용 */
    // @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindResult,
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

        /**  로그인 성공 처리
         * 세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관
         * */
        sessionManager.createSession(loginMember, response);
        return "redirect:/";
    }

    // 로그아웃: 쿠키의 시간을 없앤다.
    // @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    // 로그아웃: 세션의 시간을 없앤다.
    // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); // 세션 제거
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);    // 만료 시간 0 설정.
        response.addCookie(cookie);
    }
}
