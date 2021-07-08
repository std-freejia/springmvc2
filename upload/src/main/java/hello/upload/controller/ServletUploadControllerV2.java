package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    /** 파일 서버 경로 가져오기 */
    @Value("${file.dir}") // application.properites 의 값을 가져올 수 있다.
    private String fileDir;

    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            /** part 에도 header가 있고, 그 안에 filename이 있다.  편의 메소드로 접근 가능 */
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }
            // 1) 편의 메서드
            //content-disposition; filename
            log.info("submittedFilename={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize()); // part body size

            // 2) 바이너리 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            /** 바이너리<->문자 변환 시, 반드시 charset을 설정해야 한다. */
            // log.info("body={}", body);

            // 3) 경로에 첨부파일 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())){
                String fullPath = fileDir + part.getSubmittedFileName(); /** 전체경로 지정 */
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);
            }

        }
       return "upload-form";
    }
}
