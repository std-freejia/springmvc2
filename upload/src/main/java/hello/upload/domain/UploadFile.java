package hello.upload.domain;

import lombok.Data;

@Data //  @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode 한번에 설정.
public class UploadFile {

    /**
     * 서로 다른 고객이 같은 파일이름을 업로드 하는 경우, 기존 파일명과 충돌날 수 있다.
     * 그래서 서버에서는 파일명이 겹치지 않도록, 내부에서 관리하는 별도의 파일명이 필요하다.
     */
    private String uploadFileName; /** 사용자가 업로드한 파일명 */
    private String storeFileName; /** 서버에 저장할 파일명 */

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
