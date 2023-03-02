package org.example;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;

class ClassLoaderLeakTest {

    private static final Logger LOGGER = Logger.getLogger(ClassLoaderLeakTest.class.getName());
    private static ClientAndServer SERVER;
    @BeforeAll
    static void init() {
        SERVER = startClientAndServer(8080);
        SERVER.when(
            request()
                .withMethod("GET")
                .withPath("/hello")
        )
        .respond(
            HttpResponse.response()
                .withBody("hello")
        );
    }

    @AfterAll
    static void destroy() {
        SERVER.stop();
    }

    private CustomClassLoader cl;

    @BeforeEach
    void configure() {
        cl = new CustomClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
    }

    @AfterEach
    void clean() throws Exception {
        cl.close();
    }

    @RepeatedTest(100)
    void test(RepetitionInfo repetitionInfo) throws Exception {
        if (repetitionInfo.getCurrentRepetition() % 10 == 0) {
            LOGGER.info("Progress: " + repetitionInfo.getCurrentRepetition() + " / " + repetitionInfo.getTotalRepetitions());
        }
        cl.loadClass("org.example.Main").getMethod("callTest").invoke(null);
    }
}
