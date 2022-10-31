package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.Console;
import com.service.gamestorecatalog.repository.ConsoleRepository;
import com.service.gamestorecatalog.viewModel.ConsoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Component
public class ConsoleService {

    @Autowired
    ConsoleRepository consoleRepo;

    public ConsoleService(ConsoleRepository consoleRepo) {
        this.consoleRepo = consoleRepo;
    }

    //CONSOLE SERVICE LAYER METHODS...
    public ConsoleViewModel createConsole(ConsoleViewModel consoleViewModel) {

        // Remember viewModel data was validated using JSR 303
        // Validate incoming Console Data in the view model
        if (consoleViewModel == null) throw new IllegalArgumentException("No Console is passed! Game object is null!");

        Console console = new Console();
        console.setModel(consoleViewModel.getModel());
        console.setManufacturer(consoleViewModel.getManufacturer());
        console.setMemoryAmount(consoleViewModel.getMemoryAmount());
        console.setProcessor(consoleViewModel.getProcessor());
        console.setPrice(consoleViewModel.getPrice());
        console.setQuantity(consoleViewModel.getQuantity());

        return buildConsoleViewModel(consoleRepo.save(console));
    }

    public ConsoleViewModel getConsoleById(long id) {
        Optional<Console> console = consoleRepo.findById(id);
        if (console == null)
            return null;
        else
            return buildConsoleViewModel(console.get());
    }

    public void updateConsole(ConsoleViewModel consoleViewModel) {

        //Validate incoming Console Data in the view model
        if (consoleViewModel == null)
            throw new IllegalArgumentException("No console data is passed! Console object is null!");

        //make sure the Console exists. and if not, throw exception...
        if (this.getConsoleById(consoleViewModel.getId()) == null)
            throw new IllegalArgumentException("No such console to update.");

        Console console = new Console();
        console.setId(consoleViewModel.getId());
        console.setModel(consoleViewModel.getModel());
        console.setManufacturer(consoleViewModel.getManufacturer());
        console.setMemoryAmount(consoleViewModel.getMemoryAmount());
        console.setProcessor(consoleViewModel.getProcessor());
        console.setPrice(consoleViewModel.getPrice());
        console.setQuantity(consoleViewModel.getQuantity());

        consoleRepo.save(console);
    }

    public void deleteConsole(long id) {
        consoleRepo.deleteById(id);
    }

    public List<ConsoleViewModel> getConsoleByManufacturer(String manufacturer) {
        List<Console> consoleList = consoleRepo.findAllByManufacturer(manufacturer);
        List<ConsoleViewModel> cvmList = new ArrayList<>();

        if (consoleList == null)
            return null;
        else
            consoleList.stream().forEach(c -> cvmList.add(buildConsoleViewModel(c)));
        return cvmList;
    }

    public List<ConsoleViewModel> getAllConsoles() {
        List<Console> consoleList = consoleRepo.findAll();
        List<ConsoleViewModel> cvmList = new ArrayList<>();

        if (consoleList == null)
            return null;
        else
            consoleList.stream().forEach(c -> cvmList.add(buildConsoleViewModel(c)));
        return cvmList;
    }

    //Helper Methods...

    public ConsoleViewModel buildConsoleViewModel(Console console) {
        ConsoleViewModel consoleViewModel = new ConsoleViewModel();
        consoleViewModel.setId(console.getId());
        consoleViewModel.setModel(console.getModel());
        consoleViewModel.setManufacturer(console.getManufacturer());
        consoleViewModel.setMemoryAmount(console.getMemoryAmount());
        consoleViewModel.setProcessor(console.getProcessor());
        consoleViewModel.setPrice(console.getPrice());
        consoleViewModel.setQuantity(console.getQuantity());

        return consoleViewModel;
    }

}
