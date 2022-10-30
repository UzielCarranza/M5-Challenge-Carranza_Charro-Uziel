package com.service.gamestoreinvoicing.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gamestore-catalog")
public interface GameStoreCatalogClient {

        @GetMapping("/console")
        public String getTest();
}
