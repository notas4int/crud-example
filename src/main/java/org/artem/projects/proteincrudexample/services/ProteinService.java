package org.artem.projects.proteincrudexample.services;

import org.artem.projects.proteincrudexample.dtos.ProteinCreatingDTO;
import org.artem.projects.proteincrudexample.entities.Protein;

public interface ProteinService {
    Protein findProteinById(long id);

    Protein saveProtein(ProteinCreatingDTO protein);

    Protein updateProtein(Protein protein);

    boolean deleteProteinById(long id);
}
