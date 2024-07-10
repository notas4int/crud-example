package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.entities.Protein;

public interface ProteinService {
    Protein findProteinById(long id);

    int saveProtein(Protein protein);

    int updateProtein(Protein protein);

    int deleteProteinById(long id);
}
