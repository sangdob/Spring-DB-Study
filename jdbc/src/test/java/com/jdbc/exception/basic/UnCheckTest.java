package com.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class UnCheckTest {

    @Test
    public void UnCallCatch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_Throw() {
        Service service = new Service();

        assertThatThrownBy(() -> {
            service.callCatch();
        }).isInstanceOf(MyUnCheckedException.class);
    }

    @Test
    void uncallThrow(){
        Repository repository = new Repository();
        repository.call();
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                log.info("exception message = {}", e.getMessage(), e);
            }
        }
    }

    static class Repository {
        public void call() throws MyUnCheckedException {
            throw new MyUnCheckedException("ex");
        }
    }

}
