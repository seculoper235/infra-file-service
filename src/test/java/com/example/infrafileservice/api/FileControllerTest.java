package com.example.infrafileservice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@WebMvcTest
public class FileControllerTest {

    @Test
    @DisplayName("파일 업로드 시, FileTable에 데이터가 저장되고 S3에 파일이 업로드 된다")
    public void upload_file_save_table_upload_s3() {
        //
    }

    @Test
    @DisplayName("파일 매핑 시, 파일 사용 정보를 업데이트 한다")
//    @DisplayName("파일 매핑 시, 기존 데이터들의 temp 컬럼은 모두 true로 바꾸고 새로운 데이터들은 false로 바꾼다")
    public void mapping_file_update_file_used_data() {
        //
    }

    @Test
    @DisplayName("파일 삭제 시, FileTable의 데이터 temp 값이 true로 바뀐다")
    public void delete_file_delete_table_delete_s3() {
        //
    }

    @Test
    @DisplayName("스케줄 시간마다 FileTable의 데이터 temp 값이 true인 데이터와 S3 파일을 삭제한다")
    public void schedule_time_delete_unmapped_file() {
        //
    }
}
