package hello.upload.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {  /** form 에서 담아올 데이터들 */

    private Long itemId;
    private String itemName;
    private MultipartFile attachFile; //@ModelAttribute
    private List<MultipartFile> imageFiles; // 이미지 다중 업로드
}
