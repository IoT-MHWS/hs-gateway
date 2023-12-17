package artgallery.geteway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import artgallery.geteway.client.UserServiceClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

  private final UserServiceClient userServiceClient;

  public AuthFilter(@Lazy UserServiceClient userServiceClient) {
    super(Config.class);
    this.userServiceClient = userServiceClient;
  }

  @Override
  public GatewayFilter apply(Config config) {

    return (exchange, chain) -> {
      String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if (!StringUtils.hasText(token)) {
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token is not specified"));
      }
      return userServiceClient.getUserDetails(token)
          .doOnError(exc -> log.error(exc.getMessage()))
          .flatMap(details -> {
            exchange.getRequest().mutate()
                .header("X-User-Id", details.getId().toString())
                .header("X-User-Name", details.getLogin())
                .header("X-User-Authorities", String.join(",", details.getAuthorities()))
                .build();
            return chain.filter(exchange);
          });
    };
  }


  public static class Config {

  }

}
