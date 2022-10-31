package com.service.gamestorecatalog.controller;

import com.service.gamestorecatalog.service.ConsoleService;
import com.service.gamestorecatalog.viewModel.ConsoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/console")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ConsoleController {

    @Autowired
    private ConsoleService consoleService;

    public ConsoleController(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsoleViewModel createConsole(@RequestBody @Valid ConsoleViewModel consoleViewModel) {
        consoleViewModel = consoleService.createConsole(consoleViewModel);
        return consoleViewModel;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsoleViewModel getConsole(@PathVariable("id") long consoleId) {
        ConsoleViewModel consoleViewModel = consoleService.getConsoleById(consoleId);
        if (consoleViewModel == null) {
            throw new IllegalArgumentException("Console could not be retrieved for id " + consoleId);
        } else {
            return consoleViewModel;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConsole(@RequestBody @Valid ConsoleViewModel consoleViewModel) {

        if (consoleViewModel == null || consoleViewModel.getId() < 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        } else if (consoleViewModel.getId() > 0) {
            consoleService.updateConsole(consoleViewModel);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable("id") long consoleId) {
        consoleService.deleteConsole(consoleId);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsoleViewModel> getConsoleByManufacturer(@PathVariable("manufacturer") String manu) {
        List<ConsoleViewModel> cvmByManufacturer = consoleService.getConsoleByManufacturer(manu);
        if (cvmByManufacturer == null || cvmByManufacturer.isEmpty()) {
            throw new IllegalArgumentException("No consoles, manufactured by " + manu + ", were found");
        } else
            return cvmByManufacturer;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ConsoleViewModel> getAllConsoles() {
        List<ConsoleViewModel> cvmByManufacturer = consoleService.getAllConsoles();
        if (cvmByManufacturer == null || cvmByManufacturer.isEmpty()) {
            throw new IllegalArgumentException("No consoles were found");
        } else
            return cvmByManufacturer;
    }
}
