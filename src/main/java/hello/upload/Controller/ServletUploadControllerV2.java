package hello.upload.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    //Spring으로 import해야해, application.properties에 있는 파일 경로를 가져오기 위해서
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile(){

        return "upload-form";
    }

    @PostMapping("/upload")
    public  String saveFileV1(MultipartHttpServletRequest request) throws ServletException, IOException {
        //MultipartHttpServeltRequest는 HttpServletRequest의 자식 인터페이스이다.
        //멀티파트와 관련된 추가 기능을 제공한다.
        log.info("request={}", request);//org.springframework.web.multipart.support.StandardMultipartHttpServletRequest@6c314ccd

        String itemName = request.getParameter("itemName");//입력한 파일명
        log.info("itemName={}", itemName);

        /**
         * request.getParts() :
         *  multipart/form-data 전송 방식에서 각각 나누어진 부분을 받아서 확인할 있다.
         * */
        Collection<Part> parts = request.getParts();
        log.info("parts={}",parts);
        // parts=[org.apache.catalina.core.ApplicationPart@620771c1, org.apache.catalina.core.ApplicationPart@f40b7ea]
        // 2개가 들어있지?, 이걸 돌려서 문자도 꺼내고, 파일도 꺼내는 거야.

        for(Part part: parts){//part도 header와 body로 각각 분리된다.
            log.info("=== PART ===");
            log.info("name={}", part.getName());//요청 쿼리파라미터 key

            //part에 헤더들의 값을 출력해부자.
            Collection<String> headerNames = part.getHeaderNames();
            for(String headerName : headerNames){
                log.info("header {}: {}",headerName, part.getHeader(headerName));
                //headercontent-disposition: form-data; name="itemName"
            }

            //편의 메서드 제공
            //전송버튼을 누르면 콘솔창에
            //content-disposition;에 보면 name="file; filename="image.png" <- 문자가 아닌경우 2개가 넘어가.
            log.info("submittedFilename={}",part.getSubmittedFileName());
            log.info("size={}", part.getSize());//part body size

            //데이터 읽기
            //(바이너리 데이터를 문자로 바꿀 때, Charset은 반드시 정의해줘야한다.)
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//데이터를 직접 읽어오려면 이렇게 작성하면 됨.
            log.info("body={}",body);//바이너리 파일을 억지로 String으로 바꿔서 콘솔창에서 깨져보이는거지

            //파일에 저장하기
            //hasText() : Util 클래스를 사용하여 널체크, 공백 체크 등 지루한 개발 작업을 비교적 편리하게 개발
            if(StringUtils.hasText(part.getSubmittedFileName())){//실제 파일이 있는지 확인
                String fullPath =  fileDir + part.getSubmittedFileName();//디렉토리명 + 파일명
                log.info("파일 저장 fullPath={}",fullPath);
                //파일 저장 fullPath=C:/Users/hyeon/Desktop/infrenTest/file/c.png
                part.write(fullPath);//실제 경로에 저장이 된다.(방법은 스프링 버전차이에 따라 다르다.)
            }
        }
        return "upload-form";
    }
}
