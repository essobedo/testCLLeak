package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;

class ClassLoaderLeakTest {

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
    void test() throws Exception {
        cl.loadClass("org.example.Main").getMethod("callTest").invoke(null);
    }
}
