package hello.upload.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {//상품 저장용 폼

    private Long itemId;
    private String itemName;
    private MultipartFile attachFile;       // 멀티파트는 @ModelAttribute를 사용할 수 있다.
    private List<MultipartFile> imageFiles; // 이미지를 다중 업로드 하기 위해 MutipartFile을 사용
}
