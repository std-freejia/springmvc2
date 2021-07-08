package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }

    /**
     * 업로드 사이즈 제한
     * spring.servlet.multipart.max-file-size=1MB // 파일하나의 최대 사이즈
     * spring.servlet.multipart.max-request-size=10MB  // 여러파일의 총합 제한
     *
     * 애플리케이션의 서블릿 컨테이너가 멀티파트 관련 처리를 못하게 할 수 있음
     * spring.servlet.multipart.enabled=false
     */

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        return "upload-form";
    }
}
