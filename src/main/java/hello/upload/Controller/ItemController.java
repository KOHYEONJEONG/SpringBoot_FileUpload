package hello.upload.Controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemForm;
import hello.upload.domain.ItemRepository;

import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {//최종

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public  String newItem(@ModelAttribute ItemForm form){
        return "item-form";
    }


    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        //MultipartFile attachFile = form.getAttachFile();
        //UploadFile uploadFile = fileStore.storeFile(attachFile);//업로드 파일 반환
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        //List<MultipartFile> imageFiles = form.getImageFiles();
        //List<UploadFile> uploadFiles = fileStore.storeFiles(imageFiles);
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장(실제로 db에는 파일을 저장하는게 아니라 , 경로를 저장하는 거다)
        //fullPath를 저장한다기 보다, 기준 동일한 경로는 지정해두고, 나머지 경로만 저장하는 거다.
        Item item = new Item();
        item.setItemName(form.getItemName());;
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        //
        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";//55번줄은 이 줄 때문에 받은거야
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model){
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    //보안에 좀 취약
    //이미지가 깨져서 보이잖아? 아래처럼 작성해줘야 이미지들이 안깨짐(액박x)
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downLoadImage(@PathVariable String filename) throws MalformedURLException {
        //file:/Users/.../uuid.png

        //file: <-- 내부파일에 접근해라~
       return   new UrlResource("file:"+fileStore.getFullPath(filename)); //경로에 접근하여 스트림으로 반환
    }

    //id를 조회해서 아무나 경로를 알아도 못보게!
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {

        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName(); // 다운로드 받으려면 필요
        UrlResource urlResource = new UrlResource("file:"+fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}",uploadFileName);

        //한글깨짐 방지(파일명 utf-8 인코딩)
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);//UTF_8외에도 여러가지 있음.
        //규약
        String contentDisposition = "attachment; filename=\""+encodedUploadFileName+"\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //95번줄이 있어야 다운로드된다.(없으면 http 메시지 바디에 파일내용들이 뿌려지기만 함)
                .body(urlResource);
    }


}
