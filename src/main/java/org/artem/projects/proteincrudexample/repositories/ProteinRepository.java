package org.artem.projects.proteincrudexample.repositories;

import org.artem.projects.proteincrudexample.entities.Protein;

public interface ProteinRepository {
    Protein getProtein(long id);
    int createProtein(Protein protein);
    int updateProtein(Protein protein);
    int deleteProtein(long id);
}
