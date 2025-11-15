package org.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class FlorRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Flor> florRowMapper = (rs, rowNum) ->
            new Flor(rs.getString("id"), rs.getString("nome"));

    FlorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Flor> findAll() {
        return jdbcTemplate.query("SELECT id, nome FROM flores", florRowMapper);
    }

    Optional<Flor> findById(String id) {
        List<Flor> results = jdbcTemplate.query(
                "SELECT id, nome FROM flores WHERE id = ?",
                florRowMapper,
                id
        );
        return results.stream().findFirst();
    }

    Flor save(Flor flor) {
        jdbcTemplate.update(
                "INSERT INTO flores (id, nome) VALUES (?, ?)",
                flor.getId(),
                flor.getNome()
        );
        return flor;
    }

    int update(String id, Flor flor) {
        return jdbcTemplate.update(
                "UPDATE flores SET nome = ? WHERE id = ?",
                flor.getNome(),
                id
        );
    }

    void deleteById(String id) {
        jdbcTemplate.update("DELETE FROM flores WHERE id = ?", id);
    }
}