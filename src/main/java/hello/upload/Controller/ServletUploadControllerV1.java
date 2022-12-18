package hello.upload.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {//v1답게 점진적으로 진화될거라~

    @GetMapping("/upload")
    public String newFile(){

        return "upload-form";
    }

    @PostMapping("/upload")
    public  String sabeFileV1(MultipartHttpServletRequest request) throws ServletException, IOException {
        //MultipartHttpServeltRequest는 HttpServletRequest의 자식 인터페이스이다.
        //멀티파트와 관련된 추가 기능을 제공한다.
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        /**
         * request.getParts() :
         *  multipart/form-data 전송 방식에서 각각 나누어진 부분을 받아서 확인할 있다.
         * */
        Collection<Part> parts = request.getParts();
        log.info("parts={}",parts);
        // parts=[org.apache.catalina.core.ApplicationPart@620771c1, org.apache.catalina.core.ApplicationPart@f40b7ea]
        // 2개가 들어있지?, 이걸 돌려서 문자도 꺼내고, 파일도 꺼내는 거야.

        return "upload-form";
    }
}
