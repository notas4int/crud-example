package org.artem.projects.proteincrudexample.repositories;

import org.artem.projects.proteincrudexample.entities.Protein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProteinRepository extends JpaRepository<Protein, Long> {
}
