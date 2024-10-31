package com.itwillbs;

import com.itwillbs.entity.Manager;
import com.itwillbs.repository.ManagerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class adminsTest {
    @Autowired
    private ManagerRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    @Commit
    void admin(){
        Manager admin = Manager.builder()
                .managerId("admin")
                .pass(encoder.encode("admin123"))
                .name("admin")
                .email("admin")
                .phone("admin")
                .managerRole("ROLE_ADMIN")
                .build();
        Manager saved = adminRepository.save(admin);
        Assertions.assertThat(saved.toString())
                .as("같은지 테스트 %s", saved)
                .isEqualTo(admin.toString());
    }

    void register(){
        Manager admin = Manager.builder()
                .managerId("testid")
                .pass(encoder.encode("password"))
                .name("testNick")
                .email("testEmail")
                .phone("testphone")
                .managerRole("manager")
                .build();
        Manager saved = adminRepository.save(admin);
        Assertions.assertThat(saved.toString())
                .as("같은지 테스트 %s", saved)
                .isEqualTo(admin.toString());
    }

}
