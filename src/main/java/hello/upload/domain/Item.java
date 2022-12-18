package hello.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {//상품도메인

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;//이미지가 여러개 파일로 업로드 할 수 있기 때문에.

}

