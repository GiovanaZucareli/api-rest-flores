package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    private final FlorRepository repository;

    RestApiDemoController(FlorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    Iterable<Flor> getFlor() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Flor> getFlorById(@PathVariable String id) {
        return repository.findById(id);
    }

    @PostMapping
    Flor postFlor(@RequestBody Flor flor) {
        return repository.save(flor);
    }

    @PutMapping("/{id}")
    ResponseEntity<Flor> putFlor(@PathVariable String id, @RequestBody Flor flor) {
        int updated = repository.update(id, flor);
        return (updated == 0) ?
                new ResponseEntity<>(postFlor(new Flor(id, flor.getNome())), HttpStatus.CREATED) :
                new ResponseEntity<>(new Flor(id, flor.getNome()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteFlor(@PathVariable String id) {
        repository.deleteById(id);
    }
}

class Flor {
    private final String id;
    private String nome;

    @JsonCreator
    public Flor(@JsonProperty("id") String id,
                @JsonProperty(value = "nome", required = true) String nome) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}