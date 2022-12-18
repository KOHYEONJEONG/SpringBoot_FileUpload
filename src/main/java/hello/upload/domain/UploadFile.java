package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {//업로드 파일 정보 보관

    private String uploadFileName; //고객이 업로드한 파일명
    private String storeFileName;  //서버 내부에서 관리하는 파일명(시스템에 저장한 파일명) - 안겹치게 만들어야 해.

    /**
     * 고객이 업로드한 파일명으로 서버 내부에 파일을 저장하면 안된다.
     * 왜냐하면 서로 다른 고객이 같은 파일이름을 업로드하는 경우 기존 파일 이름과 '충돌이 날 수 있다.'
     * 서버에서는 저장할 파일명이 '겹치지 않도록' 내부에서 관리하는 별도의 파일명이 필요하다.
     * */

    public UploadFile(String uploadFileName, String storeFileName){
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}