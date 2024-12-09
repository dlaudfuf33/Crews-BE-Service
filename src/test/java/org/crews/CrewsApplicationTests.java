package org.crews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class CrewsApplicationTests {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    CrewsApplicationTests(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Test
	void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {});
    }

}
