package com.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CheckTest {

    @Test
    public void callCatch() {
        Service service = new Service();
        service.callCatch();

    }

    @Test
    void checked_Throw() {
        Service service = new Service();

        assertThatThrownBy(() -> {
            service.callCatch();
        }).isInstanceOf(MyCheckedException.class);
    }

    @Test
    void callThrow() throws Exception {
        Repository repository = new Repository();
        repository.call();
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("exception message = {}", e.getMessage(), e);
            }
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }

}
