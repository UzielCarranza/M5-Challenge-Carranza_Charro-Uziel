package com.service.gamestorecatalog.service;

import com.service.gamestorecatalog.model.Game;
import com.service.gamestorecatalog.repository.GameRepository;
import com.service.gamestorecatalog.viewModel.GameViewModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Component
public class GameService {

    private GameRepository gameRepo;

    public GameService(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    //Game service layer...
    public GameViewModel createGame(GameViewModel gameViewModel) {

        // Validate incoming Game Data in the view model.
        // All validations were done using JSR303
        if (gameViewModel == null) throw new IllegalArgumentException("No Game is passed! Game object is null!");

        Game game = new Game();
        game.setTitle(gameViewModel.getTitle());
        game.setEsrbRating(gameViewModel.getEsrbRating());
        game.setDescription(gameViewModel.getDescription());
        game.setPrice(gameViewModel.getPrice());
        game.setQuantity(gameViewModel.getQuantity());
        game.setStudio(gameViewModel.getStudio());

        gameViewModel.setId(gameRepo.save(game).getId());
        return gameViewModel;
    }

    public GameViewModel getGame(long id) {
        Optional<Game> game = gameRepo.findById(id);
        if (game == null)
            return null;
        else
            return buildGameViewModel(game.get());
    }

    public void updateGame(GameViewModel gameViewModel) {

        //Validate incoming Game Data in the view model
        if (gameViewModel == null)
            throw new IllegalArgumentException("No Game data is passed! Game object is null!");

        //make sure the game exists. and if not, throw exception...
        if (this.getGame(gameViewModel.getId()) == null)
            throw new IllegalArgumentException("No such game to update.");

        Game game = new Game();
        game.setId(gameViewModel.getId());
        game.setTitle(gameViewModel.getTitle());
        game.setEsrbRating(gameViewModel.getEsrbRating());
        game.setDescription(gameViewModel.getDescription());
        game.setPrice(gameViewModel.getPrice());
        game.setQuantity(gameViewModel.getQuantity());
        game.setStudio(gameViewModel.getStudio());

        gameRepo.save(game);
    }

    public void deleteGame(long id) {
        gameRepo.deleteById(id);
    }

    public List<GameViewModel> getGameByEsrb(String esrb) {
        List<Game> gameList = gameRepo.findAllByEsrbRating(esrb);
        List<GameViewModel> gvmList = new ArrayList<>();
        if (gameList == null)
            return null;
        else
            gameList.stream().forEach(g -> gvmList.add(buildGameViewModel(g)));
        return gvmList;
    }

    public List<GameViewModel> getGameByTitle(String title) {
        List<Game> gameList = gameRepo.findAllByTitle(title);
        List<GameViewModel> gvmList = new ArrayList<>();
        List<GameViewModel> exceptionList = null;

        if (gameList == null) {
            return exceptionList;
        } else {
            gameList.stream().forEach(g -> gvmList.add(buildGameViewModel(g)));
        }
        return gvmList;
    }

    public List<GameViewModel> getGameByStudio(String studio) {
        List<Game> gameList = gameRepo.findAllByStudio(studio);
        List<GameViewModel> gvmList = new ArrayList<>();

        if (gameList == null)
            return null;
        else
            gameList.stream().forEach(g -> gvmList.add(buildGameViewModel(g)));
        return gvmList;
    }

    public List<GameViewModel> getAllGames() {
        List<Game> gameList = gameRepo.findAll();
        List<GameViewModel> gvmList = new ArrayList<>();

        if (gameList == null)
            return null;
        else
            gameList.stream().forEach(g -> gvmList.add(buildGameViewModel(g)));
        return gvmList;
    }


    //Helper Methods...

    public GameViewModel buildGameViewModel(Game game) {

        GameViewModel gameViewModel = new GameViewModel();
        gameViewModel.setId(game.getId());
        gameViewModel.setTitle(game.getTitle());
        gameViewModel.setEsrbRating(game.getEsrbRating());
        gameViewModel.setDescription(game.getDescription());
        gameViewModel.setPrice(game.getPrice());
        gameViewModel.setStudio(game.getStudio());
        gameViewModel.setQuantity(game.getQuantity());

        return gameViewModel;
    }

}
