import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactorTest {

    // Mono 테스트 예시
    @Test
    public void testMono() {
        // Mono.just는 하나의 아이템을 포함하는 Mono를 생성합니다.
        Mono<String> mono = Mono.just("Hello, Reactor!");

        // StepVerifier를 사용하여 Mono가 예상대로 동작하는지 검증합니다.
        StepVerifier.create(mono)
                .expectNext("Hello, Reactor!")  // 기대값
                .verifyComplete();  // Mono가 완료될 때까지 검증
    }

    // Flux 테스트 예시
    @Test
    public void testFlux() {
        // Flux는 여러 아이템을 포함하는 스트림을 생성합니다.
        Flux<Integer> flux = Flux.just(1,2,3,4,5).limitRate(3);
        Consumer consumer = (a -> System.out.println(a));

        flux.subscribe(consumer);
    }

    // Flux에서 변환 작업 테스트 예시
    @Test
    public void testFluxTransformation() {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * 2);  // 각 값에 대해 2배 변환

        StepVerifier.create(flux)
                .expectNext(2)   // 1 * 2
                .expectNext(4)   // 2 * 2
                .expectNext(6)   // 3 * 2
                .expectNext(8)   // 4 * 2
                .expectNext(10)  // 5 * 2
                .verifyComplete();  // Flux가 완료될 때까지 검증
    }
}
