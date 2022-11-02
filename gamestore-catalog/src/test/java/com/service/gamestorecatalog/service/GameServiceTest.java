package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.Game;
import com.service.gamestorecatalog.repository.GameRepository;
import com.service.gamestorecatalog.viewModel.GameViewModel;
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

@RunWith(SpringRunner.class)
public class GameServiceTest {

    private GameRepository gameRepository;
    private GameService service;

    @Before
    public void setUp() throws Exception {
        setUpGameRepositoryMock();

        service = new GameService(gameRepository);
    }


    //Testing Game operations...
    @Test
    public void shouldCreateFindGame() {

        GameViewModel gameViewModel = new GameViewModel();
        gameViewModel.setTitle("Halo");
        gameViewModel.setEsrbRating("E10+");
        gameViewModel.setDescription("Puzzles and Math");
        gameViewModel.setPrice(new BigDecimal("23.99"));
        gameViewModel.setStudio("Xbox Game Studios");
        gameViewModel.setQuantity(5);
        gameViewModel = service.createGame(gameViewModel);

        GameViewModel gameViewModel2 = service.getGame(32);
        assertEquals(gameViewModel, gameViewModel2);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenCreateGameNullTitle() {

        GameViewModel gameViewModel = new GameViewModel();
        gameViewModel.setTitle(null);
        gameViewModel.setEsrbRating("E10+");
        gameViewModel.setDescription("Puzzles and Math");
        gameViewModel.setPrice(new BigDecimal("23.99"));
        gameViewModel.setStudio("Xbox Game Studios");
        gameViewModel.setQuantity(5);
        gameViewModel = service.createGame(gameViewModel);

        GameViewModel gameViewModel2 = service.getGame(32);

        assertEquals(gameViewModel, gameViewModel2);
    }

    @Test
    public void shouldUpdateGame() {

        GameViewModel game = new GameViewModel();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = service.createGame(game);

        game.setPrice(new BigDecimal("20.99"));
        game.setQuantity(3);
        service.updateGame(game);

        verify(gameRepository, times(2)).save(any(Game.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateGameInvalidId() {

        GameViewModel game = new GameViewModel();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = service.createGame(game);

        game.setPrice(new BigDecimal("20.99"));
        game.setQuantity(3);

        //set game id to invalid id...
        game.setId(game.getId()+1);
        service.updateGame(game);

        System.out.println(game);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenUpdateGameNullViewModel() {
        service.updateGame(null);
    }

    @Test
    public void shouldDeleteGame() {

        GameViewModel game = new GameViewModel();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = service.createGame(game);

        service.deleteGame(game.getId());

        verify(gameRepository).deleteById(any(Long.class));

    }

    @Test
    public void shouldFindGameByEsrb() {

        List<GameViewModel> gamesByEsrb = new ArrayList<>();

        GameViewModel game1 = new GameViewModel();
        game1.setTitle("Halo");
        game1.setEsrbRating("E10+");
        game1.setDescription("Puzzles and Math");
        game1.setPrice(new BigDecimal("23.99"));
        game1.setStudio("Xbox Game Studios");
        game1.setQuantity(5);
        game1 = service.createGame(game1);
        gamesByEsrb.add(game1);

        GameViewModel gameExtra = new GameViewModel();
        gameExtra.setTitle("Tetris");
        gameExtra.setEsrbRating("E10+");
        gameExtra.setDescription("block puzzle game");
        gameExtra.setPrice(new BigDecimal("10.99"));
        gameExtra.setStudio("Dolby Studios");
        gameExtra.setQuantity(9);
        gameExtra = service.createGame(gameExtra);
        gamesByEsrb.add(gameExtra);

        List<GameViewModel> gvmFromService = service.getGameByEsrb("E10+");

        assertEquals(gamesByEsrb, gvmFromService);

        //Test Esrb with no games...
        gvmFromService = service.getGameByEsrb("E18+");
        assertEquals(gvmFromService.size(),0);

    }

    @Test
    public void shouldFindGameByTitle() {
        List<GameViewModel> gvmList = new ArrayList<>();

        GameViewModel game = new GameViewModel();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(new BigDecimal("23.99"));
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game = service.createGame(game);
        gvmList.add(game);

        GameViewModel game2 = new GameViewModel();
        game2.setTitle("Fort Lines");
        game2.setEsrbRating("M");
        game2.setDescription("Zombie shooter game");
        game2.setPrice(new BigDecimal("37.99"));
        game2.setStudio("Dolby Studios");
        game2.setQuantity(3);
        game2 = service.createGame(game2);
        gvmList.add(game2);

        List<GameViewModel> gvmFromService = service.getGameByTitle("Halo");

        //Test title with no games...
        gvmFromService = service.getGameByTitle("Shalo");
        assertEquals(gvmFromService.size(),0);
    }

    @Test
    public void shouldFindGameByStudio() {
        List<GameViewModel> gvmList = new ArrayList<>();

        GameViewModel gameExtra2 = new GameViewModel();
        gameExtra2.setTitle("Tetris");
        gameExtra2.setEsrbRating("E10+");
        gameExtra2.setDescription("block puzzle game");
        gameExtra2.setPrice(new BigDecimal("10.99"));
        gameExtra2.setStudio("Dolby Studios");
        gameExtra2.setQuantity(9);
        gameExtra2 = service.createGame(gameExtra2);
        gvmList.add(gameExtra2);

        GameViewModel gameExtra3 = new GameViewModel();
        gameExtra3.setTitle("Fort Lines");
        gameExtra3.setEsrbRating("M");
        gameExtra3.setDescription("Zombie shooter game");
        gameExtra3.setPrice(new BigDecimal("37.99"));
        gameExtra3.setStudio("Dolby Studios");
        gameExtra3.setQuantity(3);
        gameExtra3 = service.createGame(gameExtra3);
        gvmList.add(gameExtra3);

        List<GameViewModel> gvmFromService = service.getGameByStudio("Dolby Studios");
        assertEquals(gvmList, gvmFromService);

        //Test title with no games...
        gvmFromService = service.getGameByStudio("EA");
        assertEquals(gvmFromService.size(),0);
    }

    @Test
    public void shouldFindAllGames() {
        List<GameViewModel> gvmList = new ArrayList<>();

        GameViewModel newGame1 = new GameViewModel();
        newGame1.setTitle("Halo");
        newGame1.setEsrbRating("E10+");
        newGame1.setDescription("Puzzles and Math");
        newGame1.setPrice(new BigDecimal("23.99"));
        newGame1.setStudio("Xbox Game Studios");
        newGame1.setQuantity(5);

        newGame1 = service.createGame(newGame1);
        gvmList.add(newGame1);

        GameViewModel newGame2 = new GameViewModel();
        newGame2.setTitle("Tetris");
        newGame2.setEsrbRating("E10+");
        newGame2.setDescription("block puzzle game");
        newGame2.setPrice(new BigDecimal("10.99"));
        newGame2.setStudio("Dolby Studios");
        newGame2.setQuantity(9);

        newGame2 = service.createGame(newGame2);
        gvmList.add(newGame2);

        GameViewModel newGame3 = new GameViewModel();
        newGame3.setTitle("Fort Lines");
        newGame3.setEsrbRating("M");
        newGame3.setDescription("Zombie shooter game");
        newGame3.setPrice(new BigDecimal("37.99"));
        newGame3.setStudio("Dolby Studios");
        newGame3.setQuantity(3);
        newGame3 = service.createGame(newGame3);
        gvmList.add(newGame3);

        List<GameViewModel> gvmFromService = service.getAllGames();
        assertEquals(gvmList, gvmFromService);

    }


    private void setUpGameRepositoryMock() {
        gameRepository = mock(GameRepository.class);

        List<Game> gamesByEsrb = new ArrayList<>();
        List<Game> gamesByTitle = new ArrayList<>();
        List<Game> gamesByStudio = new ArrayList<>();
        List<Game> allGames = new ArrayList<>();

        //No ID in this "game" object
        Game newGame1 = new Game();
        newGame1.setTitle("Halo");
        newGame1.setEsrbRating("E10+");
        newGame1.setDescription("Puzzles and Math");
        newGame1.setPrice(new BigDecimal("23.99"));
        newGame1.setStudio("Xbox Game Studios");
        newGame1.setQuantity(5);

        Game savedGame1 = new Game();
        savedGame1.setId(32);
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);
        gamesByEsrb.add(savedGame1);
        gamesByTitle.add(savedGame1);
        allGames.add(savedGame1);

        Game newGame2 = new Game();
        newGame2.setTitle("Tetris");
        newGame2.setEsrbRating("E10+");
        newGame2.setDescription("block puzzle game");
        newGame2.setPrice(new BigDecimal("10.99"));
        newGame2.setStudio("Dolby Studios");
        newGame2.setQuantity(9);

        Game savedGame2 = new Game();
        savedGame2.setId(25);
        savedGame2.setTitle("Tetris");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("block puzzle game");
        savedGame2.setPrice(new BigDecimal("10.99"));
        savedGame2.setStudio("Dolby Studios");
        savedGame2.setQuantity(9);
        gamesByEsrb.add(savedGame2);
        gamesByStudio.add(savedGame2);
        allGames.add(savedGame2);

        Game newGame3 = new Game();
        newGame3.setTitle("Fort Lines");
        newGame3.setEsrbRating("M");
        newGame3.setDescription("Zombie shooter game");
        newGame3.setPrice(new BigDecimal("37.99"));
        newGame3.setStudio("Dolby Studios");
        newGame3.setQuantity(3);

        Game savedGame3 = new Game();
        savedGame3.setId(60);
        savedGame3.setTitle("Fort Lines");
        savedGame3.setEsrbRating("M");
        savedGame3.setDescription("Zombie shooter game");
        savedGame3.setPrice(new BigDecimal("37.99"));
        savedGame3.setStudio("Dolby Studios");
        savedGame3.setQuantity(3);
        gamesByTitle.add(savedGame3);
        gamesByStudio.add(savedGame3);
        allGames.add(savedGame3);

        doReturn(savedGame1).when(gameRepository).save(newGame1);
        doReturn(Optional.of(savedGame3)).when(gameRepository).findById(60L);
        doReturn(Optional.of(savedGame1)).when(gameRepository).findById(32L);
        doReturn(Optional.of(savedGame2)).when(gameRepository).findById(25L);
        doReturn(savedGame2).when(gameRepository).save(newGame2);
        doReturn(savedGame3).when(gameRepository).save(newGame3);

        doReturn(gamesByEsrb).when(gameRepository).findAllByEsrbRating("E10+");
        doReturn(gamesByStudio).when(gameRepository).findAllByStudio("Dolby Studios");
        doReturn(gamesByTitle).when(gameRepository).findAllByTitle("Halo");
        doReturn(allGames).when(gameRepository).findAll();

    }

}