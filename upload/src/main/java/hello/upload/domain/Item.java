package hello.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attacheFile;
    private List<UploadFile> imageFiles; // 파일목록
}
