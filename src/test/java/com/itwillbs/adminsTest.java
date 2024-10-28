package com.itwillbs;

import com.itwillbs.entity.Admin;
import com.itwillbs.repository.AdminsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class adminsTest {
    @Autowired
    private AdminsRepository adminsRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void register(){
        Admin admin = Admin.builder()
                .adminID("testid12")
                .pass(encoder.encode("password12"))
                .name("testNick")
                .email("testEmail")
                .phone("testEmail")
                .build();
        Admin saved = adminsRepository.save(admin);
        Assertions.assertThat(saved.toString())
                .as("같은지 테스트 %s", saved)
                .isEqualTo(admin.toString());
    }

}
