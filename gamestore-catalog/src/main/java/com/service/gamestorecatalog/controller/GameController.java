package com.service.gamestorecatalog.controller;

import com.service.gamestorecatalog.service.GameService;
import com.service.gamestorecatalog.viewModel.GameViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = {"http://localhost:3000"})
public class GameController {

    @Autowired
    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameViewModel createGame(@RequestBody @Valid GameViewModel gameViewModel) {
        gameViewModel = gameService.createGame(gameViewModel);
        return gameViewModel;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GameViewModel getGameInfo(@PathVariable("id") long gameId) {
        GameViewModel gameViewModel = gameService.getGame(gameId);
        if (gameViewModel == null) {
            throw new IllegalArgumentException("Game not found for id " + gameId);
        } else {
            return gameViewModel;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGame(@RequestBody @Valid GameViewModel gameViewModel) {
        gameService.updateGame(gameViewModel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("id") int gameId) {
        gameService.deleteGame(gameId);
    }

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<GameViewModel> getGamesByTitle(@PathVariable("title") String title) {
        List<GameViewModel> gamesByTitle = gameService.getGameByTitle(title);

        if (gamesByTitle == null || gamesByTitle.isEmpty()) {
            throw new IllegalArgumentException("No games were found with " + title);
        } else {
            return gamesByTitle;
        }
    }

    @GetMapping("/esrbrating/{esrb}")
    @ResponseStatus(HttpStatus.OK)
    public List<GameViewModel> getGamesByEsrbRating(@PathVariable("esrb") String esrb) {
        List<GameViewModel> gamesByEsrbRating = gameService.getGameByEsrb(esrb);

        if (gamesByEsrbRating == null || gamesByEsrbRating.isEmpty()) {
            throw new IllegalArgumentException("No games were found with ESRB Rating " + esrb);
        } else {
            return gamesByEsrbRating;
        }
    }

    @GetMapping("/studio/{studio}")
    @ResponseStatus(HttpStatus.OK)
    public List<GameViewModel> getGamesByStudio(@PathVariable("studio") String studio) {
        List<GameViewModel> gamesByStudio = gameService.getGameByStudio(studio);

        if (gamesByStudio == null || gamesByStudio.isEmpty()) {
            throw new IllegalArgumentException("No games were found from " + studio);
        } else {
            return gamesByStudio;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<GameViewModel> getAllGames() {
        List<GameViewModel> games = gameService.getAllGames();

        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("No games were found.");
        } else {
            return games;
        }
    }
}
