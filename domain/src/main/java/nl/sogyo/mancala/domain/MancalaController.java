package nl.sogyo.mancala.domain;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // allow React to talk to this server

public class MancalaController {

    private final Facade facade;

    public MancalaController() {
        this.facade = new Facade();
    }

    @GetMapping("/start")
    public Map<String, Object> getStartNewGame() {
        facade.startGame();
        return facade.getPocketsStatus();
        // Spring Boot automatically converts this Java object into JSON for React!
    }

    // listen for a GET request to http://localhost:8080/api/board
    @GetMapping("/board")
    public Map<String, Object> getBoard() {
        return facade.getPocketsStatus();
        // Spring Boot automatically converts this Java object into JSON for React!
    }

    @PostMapping("/move/{pocketNr}")
    public Map<String, Object> makeMove(@PathVariable("pocketNr") int pocketNr) {
        facade.setMoveStones(pocketNr);
        return facade.getPocketsStatus();
    }
}