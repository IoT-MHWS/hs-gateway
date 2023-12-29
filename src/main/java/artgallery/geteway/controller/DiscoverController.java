package artgallery.geteway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactivefeign.utils.Pair;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DiscoverController {

  private final DiscoveryClient discoveryClient;

  @GetMapping("/api/v1/discover")
  public Map<String, List<URI>> v3swaggerConfigurations(ServerHttpRequest request) {
    var instances = discoveryClient.getServices().stream()
        .map(service -> new Pair<>(service, discoveryClient.getInstances(service).stream().map(ServiceInstance::getUri)
            .collect(Collectors.toList())));
    HashMap<String, List<URI>> result = new HashMap<>();
    instances.forEach(instance -> {
      result.put(instance.left, instance.right);
    });
    return result;
  }

}
