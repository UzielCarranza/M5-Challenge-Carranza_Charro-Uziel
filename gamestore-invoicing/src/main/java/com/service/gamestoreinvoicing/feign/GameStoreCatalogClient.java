package com.service.gamestoreinvoicing.feign;

import com.service.gamestoreinvoicing.model.Console;
import com.service.gamestoreinvoicing.model.Game;
import com.service.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@FeignClient(name = "gamestore-catalog")
public interface GameStoreCatalogClient {


    @GetMapping("/console/{itemId}")
    Optional<Console> findConsoleById(@PathVariable long itemId);



    @GetMapping("/game/{itemId}")
    Optional<Game> findGameById(@PathVariable long itemId);


    @GetMapping("/tshirt/{itemId}")
    Optional<TShirt> findTShirtById(@PathVariable long itemId);

    @PostMapping("/tshirt")
    @ResponseStatus(HttpStatus.CREATED)
     TShirt createTShirt(@RequestBody @Valid TShirt tShirtViewModel);
}
