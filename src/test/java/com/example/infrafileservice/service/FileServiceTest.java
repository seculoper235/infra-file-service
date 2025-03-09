package com.example.infrafileservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Test
    @DisplayName("파일 업로드 시, S3에 파일이 업로드가 성공했다면 파일 정보를 반환한다")
    public void upload_file_success_s3_return_file_info() {
        //
    }

    @Test
    @DisplayName("파일 업로드 시, S3에 파일이 업로드가 실패했다면 에러를 던진다")
    public void upload_file_fail_s3_upload_return_error() {
        //
    }

    @Test
    @DisplayName("파일 매핑 시, 업데이트 된 파일 사용 정보를 반환한다")
    public void mapping_file_return_update_file_info() {
        // 파일 매핑 시, 기존 데이터들의 temp 컬럼은 모두 true로 바꾸고 새로운 데이터들은 false로 바꾼다
    }

    @Test
    @DisplayName("파일 삭제 시, S3 파일 삭제가 성공했다면 테이블 데이터도 삭제된다")
    public void delete_file_success_delete_s3_delete_table_data() {
        //
    }

    @Test
    @DisplayName("파일 삭제 시, S3 파일 삭제가 실패했다면 에러를 던진다")
    public void delete_file_fail_delete_s3_return_error() {
        //
    }

    @Test
    @DisplayName("스케줄 시간마다 파일 사용 정보에 따라 2일 경과된 데이터는 삭제된다")
    public void schedule_time_delete_unmapped_file() {
        //
    }
}
