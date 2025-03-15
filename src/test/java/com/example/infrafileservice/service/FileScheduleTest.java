package com.example.infrafileservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
public class FileScheduleTest {
    @MockitoSpyBean
    FileScheduler fileScheduler;

    @Test
    @DisplayName("스케줄 시간마다 임시 데이터가 3일 경과 되었다면 DELETE 상태로 변경된다")
    public void schedule_time_temp_file_over_3_days_change_status_delete() {
        /* 포스트를 발행하지 않고 나갔을 때 */
        // Daily_File_Scheduler) 3일 후 TEMP 상태의 데이터를 DELETE 상태로 업데이트
        await()
                .atMost(Duration.ofSeconds(3L))
                .untilAsserted(() -> verify(fileScheduler, atLeast(2))
                        .changeToDelete());
    }

    @Test
    @DisplayName("스케줄 시간마다 DELETE 상태 데이터는 삭제된다")
    public void schedule_time_delete_status_file_delete_data() {
        /* 스케줄러가 파일을 삭제할 때 */
        // Daily_File_Scheduler) DELETE 상태의 데이터를 모두 삭제
        await()
                .atMost(Duration.ofSeconds(3L))
                .untilAsserted(() -> verify(fileScheduler, atLeast(2))
                        .deleteFile());
    }
}
