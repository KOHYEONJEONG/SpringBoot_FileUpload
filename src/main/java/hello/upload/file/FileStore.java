package hello.upload.file;

import hello.upload.domain.UploadFile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component//중요
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName){

        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFiles){
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    //파일 저장하기
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        //EX) image.png

        //서버에 저장할 파일명
        String storeFileName = createStoreFileName(originalFilename);

        //전송
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }
    private String createStoreFileName(String originFilename){//서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID 를 사용해서 충돌하지 않도록 한다
        String ext = extractExt(originFilename); //확장명만
        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();//"qwe-qwe-123-qwe-qwe"

        return uuid + "." + ext;
    }

    private String extractExt(String originFilename){
        //확장명만 추출
        int pos = originFilename.lastIndexOf(".");
        String ext = originFilename.substring(pos+1);
        return ext;
    }

}
