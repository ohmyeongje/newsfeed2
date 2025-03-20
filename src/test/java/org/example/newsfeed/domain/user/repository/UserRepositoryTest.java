package org.example.newsfeed.domain.user.repository;

import org.example.newsfeed.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원저장_및_조회_성공() {
        User user = new User("test@example.com", "testUser", "testPassword");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getName());
    }

    @Test
    public void 이메일로_사용자_조회() {
        User user = new User("test@example.com", "testUser", "testPassword");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }
}
