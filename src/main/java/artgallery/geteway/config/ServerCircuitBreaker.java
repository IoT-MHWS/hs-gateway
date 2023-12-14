package artgallery.geteway.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import lombok.RequiredArgsConstructor;
import reactivefeign.client.ReactiveFeignException;

@Configuration
@RequiredArgsConstructor
public class ServerCircuitBreaker {

  private final CircuitBreakerRegistry circuitBreakerRegistry;

  @Bean
  public CircuitBreaker defaultCircuitBreaker() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
      .failureRateThreshold(50)
      .slidingWindow(10, 10, SlidingWindowType.COUNT_BASED)
      .waitDurationInOpenState(Duration.ofMillis(6000))
      .recordExceptions(ReactiveFeignException.class, IllegalArgumentException.class)
      .permittedNumberOfCallsInHalfOpenState(2)
      .build();

    return circuitBreakerRegistry.circuitBreaker("UserServiceCircuitBreaker", circuitBreakerConfig);
  }

}
