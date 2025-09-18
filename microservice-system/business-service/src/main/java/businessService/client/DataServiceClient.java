package businessService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {
    // pruebo conectividad con:
    @GetMapping("/data/hola")
    String hola();
}