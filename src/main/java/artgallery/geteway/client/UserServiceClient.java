package artgallery.geteway.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import artgallery.geteway.dto.UserDetailsDTO;
import org.springframework.web.server.ResponseStatusException;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient(name="user", path="/api/v1")
public interface UserServiceClient {

  @GetMapping("/users/current")
  @CircuitBreaker(name = "UserServiceCircuitBreaker", fallbackMethod = "fallback")
  Mono<UserDetailsDTO> getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String header);

  default Mono<UserDetailsDTO> fallback(String token, Exception ex) {
    return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage()));
  }

}
