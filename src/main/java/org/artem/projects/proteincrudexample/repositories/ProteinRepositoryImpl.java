package org.artem.projects.proteincrudexample.repositories;

import lombok.RequiredArgsConstructor;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinCreatingException;
import org.artem.projects.proteincrudexample.exceptions.ProteinDeletingException;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.artem.projects.proteincrudexample.exceptions.ProteinUpdatingException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProteinRepositoryImpl implements ProteinRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Protein getProtein(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM protein WHERE id = ?",
                BeanPropertyRowMapper.newInstance(Protein.class), id))
                .orElseThrow(() -> new ProteinNotFoundException("Protein not found"));
    }

    @Override
    public int createProtein(Protein protein) {
        int changedRows = jdbcTemplate.update("INSERT INTO protein (name, brand, cost) VALUES (?, ?, ?)",
                protein.getName(), protein.getBrand(), protein.getCost());
        if (changedRows != 1)
            throw new ProteinCreatingException("Protein isn't created");
        return changedRows;
    }

    @Override
    public int updateProtein(Protein protein) {
        int changedRows = jdbcTemplate.update("UPDATE protein SET name = ?, brand = ?, cost = ? WHERE id = ?",
                protein.getName(), protein.getBrand(), protein.getCost(), protein.getId());
        if (changedRows != 1)
            throw new ProteinUpdatingException("Protein isn't updated");
        return changedRows;
    }

    @Override
    public int deleteProtein(long id) {
        int changedRows = jdbcTemplate.update("DELETE FROM protein WHERE id = ?", id);
        if (changedRows != 1)
            throw new ProteinDeletingException("Protein isn't deleted");
        return changedRows;
    }
}
