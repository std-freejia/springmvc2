package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null){
            return "세션이 없습니다.";
        }
        //세션 있으면, 세션 데이터 출력

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId = {}", session.getId());
        log.info("getMaxInactiveInterval() = {}", session.getMaxInactiveInterval()); //세션 유효시간 초단위.
        log.info("creationTime() = {} ", new Date(session.getCreationTime())); // 세션 생성일시
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); // 마지막으로 접근한 시각
        log.info("isNew={}", session.isNew());// 방금 만든 세션인지

        return "세션 출력";

    }
}
