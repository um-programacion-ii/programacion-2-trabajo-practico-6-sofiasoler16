package client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {

}