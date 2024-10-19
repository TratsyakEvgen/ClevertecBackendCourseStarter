package ru.clevertec.session;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.entity.Session;
import ru.clevertec.session.repository.SessionRepository;

import java.time.LocalDateTime;

@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionApplicationTests {
    private static final String BASE_PATH = "http://localhost:";
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @Test
    void get_ifSessionNotExists() {
        Assertions.assertThrows(HttpClientErrorException.NotFound.class,
                () -> restTemplate.getForObject(BASE_PATH + port + "/sessions?login=test", Session.class));
    }

    @Test
    void get_ifSessionExists() {
        Session session = createSession();
        String path = BASE_PATH + port + "/sessions";

        Session actual = restTemplate.getForObject(path + "?login=test", Session.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getCreatDate());
        Assertions.assertEquals(session.getLogin(), actual.getLogin());
    }

    @Test
    void create_ifSessionNotExists() {
        String path = BASE_PATH + port + "/sessions";

        Session actual = restTemplate.postForObject(path, new SessionRequest("test"), Session.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getCreatDate());
        Assertions.assertEquals("test", actual.getLogin());
    }

    @Test
    void create_ifSessionExists() {
        Session session = createSession();
        String path = BASE_PATH + port + "/sessions";

        Session actual = restTemplate.postForObject(path, new SessionRequest("test"), Session.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getCreatDate());
        Assertions.assertEquals(session.getLogin(), actual.getLogin());
    }

    @NotNull
    private Session createSession() {
        Session session = new Session()
                .setLogin("test")
                .setCreatDate(LocalDateTime.now());
        sessionRepository.save(session);
        return session;
    }

    @AfterEach
    void deleteAll() {
        sessionRepository.deleteAll();
    }
}
