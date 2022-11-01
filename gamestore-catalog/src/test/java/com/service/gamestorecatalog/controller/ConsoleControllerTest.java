package com.service.gamestorecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.gamestorecatalog.service.ConsoleService;
import com.service.gamestorecatalog.viewModel.ConsoleViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ConsoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConsoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private ConsoleService consoleService;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

//    global set ups

    private ConsoleViewModel inConsole;
    private ConsoleViewModel outConsole;
    private ConsoleViewModel updateConsole;
    private List<ConsoleViewModel> consoleViewModelList;
    private ConsoleViewModel outConsole2 = new ConsoleViewModel();
    ;

    @Before
    public void setUp() {
        //perform the call, pass arguments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console...
        inConsole = new ConsoleViewModel();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(12);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));

        //Mock "out"put Console...
        outConsole = new ConsoleViewModel();
        outConsole.setMemoryAmount("250GB");
        outConsole.setQuantity(12);
        outConsole.setManufacturer("Sega");
        outConsole.setModel("Nintendo");
        outConsole.setProcessor("AMD");
        outConsole.setPrice(new BigDecimal("199.89"));
        outConsole.setId(15);

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console...
        updateConsole = new ConsoleViewModel();
        updateConsole.setMemoryAmount("300GB");
        updateConsole.setQuantity(12);
        updateConsole.setManufacturer("Sega");
        updateConsole.setModel("Nintendo II");
        updateConsole.setProcessor("AMD");
        updateConsole.setPrice(new BigDecimal("249.99"));
        updateConsole.setId(15);


        //Mock a list of Consoles...
        consoleViewModelList = new ArrayList<>();
        //1st Console...

        consoleViewModelList.add(outConsole);

        //2nd Console...
        outConsole2 = new ConsoleViewModel();
        outConsole2.setMemoryAmount("200GB");
        outConsole2.setQuantity(12);
        outConsole2.setManufacturer("Sony");
        outConsole2.setModel("PS2");
        outConsole2.setProcessor("AMD");
        outConsole2.setPrice(new BigDecimal("249.99"));
        outConsole2.setId(16);
        consoleViewModelList.add(outConsole2);


        // the following mocks the service layer's method "createConsoleViewModel"
        // So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.

        // Could also have written this as the below:
        // doReturn(outConsole).when(this.service).createConsole(inConsole);
        when(this.consoleService.createConsole(inConsole)).thenReturn(outConsole);

        // So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(consoleService.getConsoleById(15)).thenReturn(outConsole);


//      shouldReturn204StatusWithGoodUpdate test case uses it
        //So we are mocking (not executing the service layer) since we are testing the controller here.
        //Remember: we are testing the code of the CONTROLLER methods.
        doNothing().when(consoleService).updateConsole(updateConsole);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        //Remember: we are testing the code of the CONTROLLER methods.
        doNothing().when(consoleService).deleteConsole(15);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        //Remember: we are testing the methodse in the CONTROLLER.
        when(consoleService.getConsoleByManufacturer("Sony")).thenReturn(consoleViewModelList);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        //Remember: we are testing the methodse in the CONTROLLER.
        when(consoleService.getAllConsoles()).thenReturn(consoleViewModelList);
    }


    @Test
    public void shouldReturnNewConsoleOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(outConsole))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturnConsoleById() throws Exception {

        // Note the way we're passing argument in the Get...
        // Note how to expect a certain value in the returned JSON object.
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/console/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(15));
    }

    @Test
    public void shouldReturn204StatusWithGoodUpdate() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(updateConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldReturn404StatusWithBadIdUpdateRequest() throws Exception {

//      shouldReturn404StatusWithBadIdUpdateRequest test case uses it
        //mock call to controller and force an exception
        doThrow(new IllegalArgumentException("Console not found. Unable to update")).when(consoleService).updateConsole(updateConsole);


//        ARRANGE
        updateConsole.setId(0);//<--pretend this is a bad id that does not match any existing Console...
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound()); //Expected response status code.
    }

    @Test
    public void shouldDeleteConsoleReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/console/{id}", 15))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldReturnConsoleByManufacturer() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/console/manufacturer/{manufacturer}", "Sony")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(consoleViewModelList)));
    }

    @Test
    public void shouldReturnAllConsoles() throws Exception {


        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(consoleViewModelList)));

        when(consoleService.getAllConsoles()).thenReturn(null);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound());
    }
//
//    //Testing bad cases...
//
//    @org.junit.Test
//    public void shouldFailCreateConsoleWithInvalidQuantity() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(51000);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("199.89"));
//
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        when(this.storeServiceLayer.createConsole(inConsole)).thenReturn(null);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
//
//        //Mock "in"coming Console  with > 50,000 quantity
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(50001);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("199.89"));
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
//    }
//
//    @org.junit.Test
//    public void shouldFailCreateConsoleWithInvalidPrice() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with null price
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(null);
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        when(this.storeServiceLayer.createConsole(inConsole)).thenReturn(null);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with no price
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("1000.00"));
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with no price
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(BigDecimal.ZERO);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailCreateConsoleInvalidManufacturer() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer(null);
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        when(this.storeServiceLayer.createConsole(inConsole)).thenReturn(null);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailCreateConsoleInvalidModel() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        when(this.storeServiceLayer.createConsole(inConsole)).thenReturn(null);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with 0 quantity
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel(null);
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailUpdateConsoleInvalidModel() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//        inConsole.setId(15);
//
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        doNothing().when(this.storeServiceLayer).updateConsole(inConsole);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with 0 quantity
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel(null);
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//        inConsole.setId(15);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailUpdateConsoleInvalidQuantity() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(0);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("199.89"));
//        inConsole.setId(15);
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        doNothing().when(this.storeServiceLayer).updateConsole(inConsole);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with > 50,000 quantity
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(50001);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("199.89"));
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailUpdateConsoleInvalidPrice() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with null price
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(null);
//        inConsole.setId(15);
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        doNothing().when(this.storeServiceLayer).updateConsole(inConsole);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with no price
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("1000.00"));
//        inConsole.setId(15);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//
//        //Mock "in"coming Console  with no price
//        inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer("Sega");
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(BigDecimal.ZERO);
//        inConsole.setId(15);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//
//    @org.junit.Test
//    public void shouldFailUpdateConsoleInvalidManufacturer() throws Exception {
//
//        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
//        // from/to JSON format.
//
//        //Mock "in"coming Console  with 0 quantity
//        ConsoleViewModel inConsole = new ConsoleViewModel();
//        inConsole.setMemoryAmount("250GB");
//        inConsole.setQuantity(2);
//        inConsole.setManufacturer(null);
//        inConsole.setModel("Nintendo");
//        inConsole.setProcessor("AMD");
//        inConsole.setPrice(new BigDecimal("10.99"));
//        inConsole.setId(15);
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        doNothing().when(this.storeServiceLayer).updateConsole(inConsole);
//
//        //ResultActions x = mockMvc.perform(
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/console")
//                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
//        ;
//    }
//    @org.junit.Test
//    public void shouldFailGetConsoleWithBadId() throws Exception{
//        //Mock "out"put Console...
//        ConsoleViewModel outConsole = new ConsoleViewModel();
//        outConsole.setMemoryAmount("250GB");
//        outConsole.setQuantity(12);
//        outConsole.setManufacturer("Sega");
//        outConsole.setModel("Nintendo");
//        outConsole.setProcessor("AMD");
//        outConsole.setPrice(new BigDecimal("199.89"));
//        outConsole.setId(15);
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        // Remember: we are testing the code of the CONTROLLER methods.
//        when(storeServiceLayer.getConsoleById(16)).thenReturn(null);
//
//        mockMvc.perform( MockMvcRequestBuilders
//                        .get("/console/{id}", 16)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isNotFound())
//        ;
//
//    }
//    @Test
//    public void shouldFailGetConsoleByManufacturerWithInvalidManufacturer() throws Exception {
//
//        //no need to create dummy data since we are returning null anyway.
//        List<ConsoleViewModel> consoleViewModelList = new ArrayList<>();
//
//        //the following mocks the service layer's method "createConsoleViewModel"
//        //So we are mocking (not executing the service layer) since we are testing the controller here.
//        //Remember: we are testing the code of the CONTROLLER methods.
//        when(storeServiceLayer.getConsoleByManufacturer("Sony")).thenReturn(null);
//
//        mockMvc.perform( MockMvcRequestBuilders
//                        .get("/console/manufacturer/{manufacturer}", "Sony")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
//                .andExpect(status().isNotFound())
//        ;
//    }


}