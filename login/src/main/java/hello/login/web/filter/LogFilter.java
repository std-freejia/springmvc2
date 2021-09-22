package hello.login.web.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.LogRecord;

@Slf4j
public class LogFilter implements Filter {

    @Override // init() 실행 초기
    public void init(FilterConfig filterConfig) throws ServletException {
       log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter"); // 고객의 요청이 올 때마다 doFilter 가 동작한다.

        HttpServletRequest httpRequest =(HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 요청 온 것을 구분하기 위해 UUID
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}] [{}]", uuid, requestURI);

            /** [중요] 다음 필터 호출해주기 */
            chain.doFilter(request,response);
        }catch(Exception e){
            throw e;
        }finally { // 항상 호출된다.
            log.info("RESPONSE [{}] [{}]", uuid, requestURI);
        }
    }

    @Override // destroy() 종료 전
    public void destroy() {
        log.info("log filter destroy");
    }
}
