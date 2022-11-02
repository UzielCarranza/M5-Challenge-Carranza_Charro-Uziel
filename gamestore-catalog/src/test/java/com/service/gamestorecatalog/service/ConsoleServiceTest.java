package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.Console;
import com.service.gamestorecatalog.repository.ConsoleRepository;
import com.service.gamestorecatalog.viewModel.ConsoleViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;


@RunWith(SpringRunner.class)
public class ConsoleServiceTest {


    private ConsoleRepository consoleRepository;

    private ConsoleService service;

    @Before
    public void setUp() throws Exception {
        setUpConsoleRepositoryMock();

        service = new ConsoleService(consoleRepository);
    }

    //    Testing Console Operations...
    @Test
    public void shouldCreateGetConsole() {

        ConsoleViewModel console = new ConsoleViewModel();
        console.setModel("Playstation");
        console.setManufacturer("Sony");
        console.setMemoryAmount("120gb");
        console.setProcessor("Intel I7-9750H");
        console.setPrice(new BigDecimal("299.99"));
        console.setQuantity(4);
        console = service.createConsole(console);

        ConsoleViewModel console1 = service.getConsoleById(console.getId());
        assertEquals(console, console1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateConsoleWithNullViewModel() {

        ConsoleViewModel console = new ConsoleViewModel();

        console = null;
        console = service.createConsole(console);
    }

    @Test
    public void shouldUpdateConsole() {
        ConsoleViewModel console2 = new ConsoleViewModel();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = service.createConsole(console2);

        console2.setQuantity(6);
        console2.setPrice(new BigDecimal(289.99));

        service.updateConsole(console2);

        verify(consoleRepository, times(2)).save(any(Console.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateConsoleWithNullModelView() {
        ConsoleViewModel console2 = null;

        service.updateConsole(console2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateConsoleWithBadId() {
        ConsoleViewModel console2 = new ConsoleViewModel();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = service.createConsole(console2);

        console2.setQuantity(6);
        console2.setPrice(new BigDecimal(289.99));

        //change Id to an invalid one.
        System.out.println(console2);
        console2.setId(console2.getId()+1);
        System.out.println(console2);

        service.updateConsole(console2);
    }

    @Test
    public void shouldDeleteConsole() {

        ConsoleViewModel console2 = new ConsoleViewModel();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);
        console2 = service.createConsole(console2);

        service.deleteConsole(console2.getId());

        verify(consoleRepository).deleteById(console2.getId());
    }

    @Test
    public void shouldFindConsoleByManufacturer() {
        List<ConsoleViewModel> cvmList = new ArrayList<>();

        ConsoleViewModel console2 = new ConsoleViewModel();
        console2.setModel("Playstation");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("120gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("299.99"));
        console2.setQuantity(4);

        console2 = service.createConsole(console2);
        cvmList.add(console2);

        ConsoleViewModel console3 = new ConsoleViewModel();
        console3.setModel("Xbox");
        console3.setManufacturer("Sony");
        console3.setMemoryAmount("256gb");
        console3.setProcessor("Intel I7-9750H");
        console3.setPrice(new BigDecimal("399.99"));
        console3.setQuantity(4);

        console3 = service.createConsole(console3);
        cvmList.add(console3);

        List<ConsoleViewModel> cvmFromService = service.getConsoleByManufacturer("Sony");

        assertEquals(cvmList, cvmFromService);
    }

    @Test
    public void shouldFindAllConsoles() throws Exception{
        List<ConsoleViewModel> cvmList = new ArrayList<>();

        ConsoleViewModel console1 = new ConsoleViewModel();
        console1.setModel("Playstation");
        console1.setManufacturer("Sony");
        console1.setMemoryAmount("120gb");
        console1.setProcessor("Intel I7-9750H");
        console1.setPrice(new BigDecimal("299.99"));
        console1.setQuantity(4);

        console1 = service.createConsole(console1);
        cvmList.add(console1);

        ConsoleViewModel console2 = new ConsoleViewModel();
        console2.setModel("Xbox");
        console2.setManufacturer("Sony");
        console2.setMemoryAmount("256gb");
        console2.setProcessor("Intel I7-9750H");
        console2.setPrice(new BigDecimal("399.99"));
        console2.setQuantity(4);

        console2 = service.createConsole(console2);
        cvmList.add(console2);

        ConsoleViewModel console3 = new ConsoleViewModel();
        console3.setModel("PS III");
        console3.setManufacturer("Sony");
        console3.setMemoryAmount("512Gb");
        console3.setProcessor("AMD I7-9750A");
        console3.setPrice(new BigDecimal("199.99"));
        console3.setQuantity(40);

        console3 = service.createConsole(console3);
        cvmList.add(console3);

        List<ConsoleViewModel> cvmFromService = service.getAllConsoles();

        assertEquals(cvmList.size(), cvmFromService.size());
    }

    //DAO Mocks...
    private void setUpConsoleRepositoryMock() {

        consoleRepository = mock(ConsoleRepository.class);

        List<Console> allConsoles = new ArrayList<>();
        List<Console> consoleByManufacturer = new ArrayList<>();

        Console newConsole1 = new Console();
        newConsole1.setModel("Playstation");
        newConsole1.setManufacturer("Sony");
        newConsole1.setMemoryAmount("120gb");
        newConsole1.setProcessor("Intel I7-9750H");
        newConsole1.setPrice(new BigDecimal("299.99"));
        newConsole1.setQuantity(4);

        Console savedConsole1 = new Console();
        savedConsole1.setId(40);
        savedConsole1.setModel("Playstation");
        savedConsole1.setManufacturer("Sony");
        savedConsole1.setMemoryAmount("120gb");
        savedConsole1.setProcessor("Intel I7-9750H");
        savedConsole1.setPrice(new BigDecimal("299.99"));
        savedConsole1.setQuantity(4);

        consoleByManufacturer.add(savedConsole1);
        allConsoles.add(savedConsole1);

        Console newConsole2 = new Console();
        newConsole2.setModel("Xbox");
        newConsole2.setManufacturer("Sony");
        newConsole2.setMemoryAmount("256gb");
        newConsole2.setProcessor("Intel I7-9750H");
        newConsole2.setPrice(new BigDecimal("399.99"));
        newConsole2.setQuantity(4);

        Console savedConsole2 = new Console();
        savedConsole2.setId(34);
        savedConsole2.setModel("Xbox");
        savedConsole2.setManufacturer("Sony");
        savedConsole2.setMemoryAmount("256gb");
        savedConsole2.setProcessor("Intel I7-9750H");
        savedConsole2.setPrice(new BigDecimal("399.99"));
        savedConsole2.setQuantity(4);

        consoleByManufacturer.add(savedConsole2);
        allConsoles.add(savedConsole2);

        Console newConsole3 = new Console();
        newConsole3.setModel("PS III");
        newConsole3.setManufacturer("Sony");
        newConsole3.setMemoryAmount("512Gb");
        newConsole3.setProcessor("AMD I7-9750A");
        newConsole3.setPrice(new BigDecimal("199.99"));
        newConsole3.setQuantity(40);

        Console savedConsole3 = new Console();
        savedConsole3.setId(38);
        savedConsole3.setModel("PS III");
        savedConsole3.setManufacturer("Sony");
        savedConsole3.setMemoryAmount("512Gb");
        savedConsole3.setProcessor("AMD I7-9750A");
        savedConsole3.setPrice(new BigDecimal("199.99"));
        savedConsole3.setQuantity(40);

        allConsoles.add(savedConsole3);

        doReturn(savedConsole1).when(consoleRepository).save(newConsole1);
        doReturn(savedConsole2).when(consoleRepository).save(newConsole2);
        doReturn(savedConsole3).when(consoleRepository).save(newConsole3);
        doReturn(Optional.of(savedConsole1)).when(consoleRepository).findById(40L);
        doReturn(consoleByManufacturer).when(consoleRepository).findAllByManufacturer("Sony");
        doReturn(allConsoles).when(consoleRepository).findAll();

    }

}