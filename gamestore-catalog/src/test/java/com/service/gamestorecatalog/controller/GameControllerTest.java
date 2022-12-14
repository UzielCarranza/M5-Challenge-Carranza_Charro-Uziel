package com.service.gamestorecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.gamestorecatalog.service.GameService;
import com.service.gamestorecatalog.viewModel.GameViewModel;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private GameService storeServiceLayer;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

//    global variables

    //Object to JSON in String
    private String outputJson;
    private String inputJson;
    private GameViewModel inGameViewModel;
    private GameViewModel savedGameViewModel;
    private GameViewModel savedGameViewModel2;
    private GameViewModel savedGameViewModel3;

    private List<GameViewModel> foundList;

    @Before
    public void setUp() throws Exception {
        outputJson = null;
        inputJson = null;

        //Arrange
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        inputJson = mapper.writeValueAsString(inGameViewModel);

        savedGameViewModel = new GameViewModel();
        savedGameViewModel.setTitle("Halo");
        savedGameViewModel.setEsrbRating("E10+");
        savedGameViewModel.setDescription("Puzzles and Math");
        savedGameViewModel.setPrice(new BigDecimal("23.99"));
        savedGameViewModel.setStudio("Xbox Game Studios");
        savedGameViewModel.setQuantity(5);
        savedGameViewModel.setId(51);

        outputJson = mapper.writeValueAsString(savedGameViewModel);

        //Arrange
        savedGameViewModel2 = new GameViewModel();
        savedGameViewModel2.setTitle("Gof of War");
        savedGameViewModel2.setEsrbRating("E10+");
        savedGameViewModel2.setDescription("Puzzles and Math");
        savedGameViewModel2.setPrice(new BigDecimal("23.99"));
        savedGameViewModel2.setStudio("Xbox Game Studios");
        savedGameViewModel2.setQuantity(5);
        savedGameViewModel2.setId(52);

        //Arrange
        savedGameViewModel3 = new GameViewModel();
        savedGameViewModel3.setTitle("Halo");
        savedGameViewModel3.setEsrbRating("E10+");
        savedGameViewModel3.setDescription("Puzzles and Math");
        savedGameViewModel3.setPrice(new BigDecimal("23.99"));
        savedGameViewModel3.setStudio("A&E");
        savedGameViewModel3.setQuantity(5);
        savedGameViewModel3.setId(56);

        foundList = new ArrayList();
        foundList.add(savedGameViewModel);

        //Mock call to service layer...
        when(storeServiceLayer.createGame(inGameViewModel)).thenReturn(savedGameViewModel);

        // Mocking DAO response
        // This is another way to mock using mockito
        // same as doReturn(gameViewModel).when(storeServiceLayer).getGame(8);
        // We could also set up our mocks in a separate method, if we so chose
        when(storeServiceLayer.getGame(8)).thenReturn(inGameViewModel);

        when(storeServiceLayer.getGameByTitle("Halo")).thenReturn(foundList);

        when(storeServiceLayer.getGameByTitle("not there")).thenReturn(null);

        when(storeServiceLayer.getGameByEsrb("10+")).thenReturn(foundList);

        when(storeServiceLayer.getGameByEsrb("not there")).thenReturn(null);

    }

    @Test
    public void shouldCreateGame() throws Exception {

        //Act & Assert
        this.mockMvc.perform(post("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnGameInfo() throws Exception {

        //Arrange
        inGameViewModel.setId(8);

        outputJson = mapper.writeValueAsString(inGameViewModel);

        //Act & Assert
        this.mockMvc.perform(get("/game/8"))
                .andDo(print())
                .andExpect(status().isOk())
                //use the objectmapper output with the json method
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldFailGetGameBadIdReturns404() throws Exception {

        long idForGameThatDoesNotExist = 100;
        when(storeServiceLayer.getGame(idForGameThatDoesNotExist)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/" + idForGameThatDoesNotExist))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateGame() throws Exception {

        //Arrange
        inGameViewModel.setId(1);
        inGameViewModel.setQuantity(5);

        //Mock call to service layer...
        //Nothing to mock!
        //Checking checking for the correct response status code
        doNothing().when(storeServiceLayer).updateGame(inGameViewModel);

        //Act & Assert
        this.mockMvc.perform(put("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteGame() throws Exception {

        //Arrange
        inGameViewModel.setId(62);

        //Mock call to service layer...
        //Nothing to mock!
        //Checking checking for the correct response status code
        doNothing().when(storeServiceLayer).deleteGame(62);

        //Act & Assert
        this.mockMvc.perform(delete("/game/62"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetGamesByTitle() throws Exception {

        outputJson = mapper.writeValueAsString(foundList);
//
        //Act & Assert
        this.mockMvc.perform(get("/game/title/{title}", "Halo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Act & Assert
        this.mockMvc.perform(get("/game/title/{title}}", "not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetGamesByEsrbRating() throws Exception {

        foundList.add(savedGameViewModel2);
        outputJson = mapper.writeValueAsString(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game/esrbrating/10+"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Act & Assert
        this.mockMvc.perform(get("/game/esrbrating/{esrb}", "not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetGamesByStudio() throws Exception {

        foundList.add(savedGameViewModel2);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to service layer...
        when(storeServiceLayer.getGameByStudio("Xbox Game Studios")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game/studio/Xbox Game Studios"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
//
        //Mock call to service layer...
        when(storeServiceLayer.getGameByStudio("not there")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/studio/{A&E}", "not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllGames() throws Exception{
        //Object to JSON in String
        String outputJson = null;

        //Arrange
        GameViewModel savedGameViewModel1 = new GameViewModel();
        savedGameViewModel1.setTitle("Halo");
        savedGameViewModel1.setEsrbRating("E10+");
        savedGameViewModel1.setDescription("Puzzles and Math");
        savedGameViewModel1.setPrice(new BigDecimal("23.99"));
        savedGameViewModel1.setStudio("A&E");
        savedGameViewModel1.setQuantity(5);
        savedGameViewModel1.setId(56);

        GameViewModel savedGameViewModel2 = new GameViewModel();
        savedGameViewModel2.setTitle("Halo I");
        savedGameViewModel2.setEsrbRating("E10+");
        savedGameViewModel2.setDescription("Puzzles and Math");
        savedGameViewModel2.setPrice(new BigDecimal("23.99"));
        savedGameViewModel2.setStudio("Xbox Game Studios");
        savedGameViewModel2.setQuantity(5);
        savedGameViewModel2.setId(51);

        GameViewModel savedGameViewModel3 = new GameViewModel();
        savedGameViewModel3.setTitle("Halo IV");
        savedGameViewModel3.setEsrbRating("E18+");
        savedGameViewModel3.setDescription("Puzzles and Math");
        savedGameViewModel3.setPrice(new BigDecimal("23.99"));
        savedGameViewModel3.setStudio("A&E");
        savedGameViewModel3.setQuantity(5);
        savedGameViewModel3.setId(77);

        List<GameViewModel> foundList = new ArrayList();
        foundList.add(savedGameViewModel1);
        foundList.add(savedGameViewModel3);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to service layer...
        when(storeServiceLayer.getAllGames()).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to service layer...
        when(storeServiceLayer.getAllGames()).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldFailCreateGameWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //title...
        GameViewModel inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.storeServiceLayer.createGame(inGameViewModel)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("");
        inGameViewModel.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("-1.00"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("60000"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(null);
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(0);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(50001);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.post("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailUpdateGameWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //title...
        GameViewModel inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.storeServiceLayer.createGame(inGameViewModel)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("-1.00"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("60000"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(null);
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(0);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("Halo");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(50001);
        inGameViewModel.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                MockMvcRequestBuilders.put("/game")
                        .content(mapper.writeValueAsString(inGameViewModel)) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailFindGamesWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //invalid id...
        GameViewModel inGameViewModel = new GameViewModel();
        inGameViewModel.setTitle("something");
        inGameViewModel.setEsrbRating("E10+");
        inGameViewModel.setDescription("Puzzles and Math");
        inGameViewModel.setPrice(new BigDecimal("23.99"));
        inGameViewModel.setStudio("Xbox Game Studios");
        inGameViewModel.setQuantity(5);
        inGameViewModel.setId(77);

        //So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.storeServiceLayer.getGame(77)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/game/77") //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                    .andExpect(status().isNotFound()); //Expected response status code.

        String badValue="bad value";
        //So we are mocking (not executing the service layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.storeServiceLayer.getGameByStudio(badValue)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/game/studio/{badValue}",badValue) //converts object to JSON and places into RequestBody
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

        when(this.storeServiceLayer.getGameByEsrb(badValue)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/game/esrbrating/{badValue}",badValue) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.


        when(this.storeServiceLayer.getGameByTitle(badValue)).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/game/title/{badValue}",badValue) //converts object to JSON and places into RequestBody
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

    }
//    808 lines of code
}