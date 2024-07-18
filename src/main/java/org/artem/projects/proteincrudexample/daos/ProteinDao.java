package org.artem.projects.proteincrudexample.daos;

import org.artem.projects.proteincrudexample.entities.Protein;

public interface ProteinDao {
    Protein findProteinById(long id);
    Protein saveProtein(Protein protein);
    Protein updateProtein(Protein protein);
    boolean deleteProteinById(long id);
}
