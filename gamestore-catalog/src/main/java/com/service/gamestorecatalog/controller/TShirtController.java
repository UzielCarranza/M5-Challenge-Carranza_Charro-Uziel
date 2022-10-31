package com.service.gamestorecatalog.controller;

import com.service.gamestorecatalog.service.TShirtService;
import com.service.gamestorecatalog.viewModel.TShirtViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tshirt")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TShirtController {

    @Autowired
    private TShirtService tShirtService;

    public TShirtController(TShirtService tShirtService) {
        this.tShirtService = tShirtService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TShirtViewModel createTShirt(@RequestBody @Valid TShirtViewModel tShirtViewModel) {
        tShirtViewModel = tShirtService.createTShirt(tShirtViewModel);
        return tShirtViewModel;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TShirtViewModel getTShirt(@PathVariable("id") int tShirtId) {
        TShirtViewModel tShirtViewModel = tShirtService.getTShirt(tShirtId);
        if (tShirtViewModel == null) {
            throw new IllegalArgumentException("T-Shirt could not be retrieved for id " + tShirtId);
        } else {
            return tShirtViewModel;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirtViewModel tShirtViewModel) {
        if (tShirtViewModel == null || tShirtViewModel.getId() < 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        } else if (tShirtViewModel.getId() > 0) {
            tShirtService.updateTShirt(tShirtViewModel);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable("id") int tShirtId) {
        tShirtService.deleteTShirt(tShirtId);
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getTShirtsBySize(@PathVariable("size") String size) {
        List<TShirtViewModel> tvmBySize = tShirtService.getTShirtBySize(size);
        if (tvmBySize == null || tvmBySize.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in size " + size);
        }
        return tvmBySize;
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getTShirtsByColor(@PathVariable("color") String color) {
        List<TShirtViewModel> tvmByColor = tShirtService.getTShirtByColor(color);
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in " + color);
        }
        return tvmByColor;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getAllTShirts() {
        List<TShirtViewModel> tvmByColor = tShirtService.getAllTShirts();
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found.");
        }
        return tvmByColor;
    }
}
