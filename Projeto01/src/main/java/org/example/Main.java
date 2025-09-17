package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@RestController
@RequestMapping("/flores")
class RestApiDemoController {
    private List<Flor> flores = new ArrayList<>();

    public RestApiDemoController() {
        flores.addAll(List.of(
                new Flor("Rosa"),
                new Flor("Girassol"),
                new Flor("Lírio"),
                new Flor("Orquídea"),
                new Flor("Tulipa")
        ));
    }

    @GetMapping
    Iterable<Flor> getFlor() {
        return flores;
    }

    @GetMapping("/{id}")
    ResponseEntity<Flor> getFlorById(@PathVariable String id) {
        Optional<Flor> optionalFlor = flores.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        return optionalFlor.map(flor -> new ResponseEntity<>(flor, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    Flor postFlor(@RequestBody Flor flor) {
        flores.add(flor);
        return flor;
    }

    @PutMapping("/{id}")
    ResponseEntity<Flor> putFlor(@PathVariable String id, @RequestBody Flor florAtualizada) {
        Optional<Flor> optionalFlor = flores.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (optionalFlor.isPresent()) {
            Flor florExistente = optionalFlor.get();
            florExistente.setNome(florAtualizada.getNome());
            return new ResponseEntity<>(florExistente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteFlor(@PathVariable String id) {
        boolean removed = flores.removeIf(c -> c.getId().equals(id));
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

class Flor {
    private String id;
    private String nome;

    public Flor() {

    }

    public Flor(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}