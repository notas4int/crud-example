package org.artem.projects.proteincrudexample.daos;

import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProteinDaoImpl implements ProteinDao {
    private final SessionFactory sessionFactory;

    public ProteinDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Protein findProteinById(long id) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var protein = session.get(Protein.class, id);
            session.getTransaction().commit();

            if (protein == null)
                throw new ProteinNotFoundException("Protein not found");

            return protein;
        }
    }

    @Override
    public Protein saveProtein(Protein protein) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(protein);
            session.getTransaction().commit();
            return protein;
        }
    }

    @Override
    public Protein updateProtein(Protein protein) {

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Protein existingProtein = session.get(Protein.class, protein.getId());
            if (existingProtein == null)
                throw new ProteinNotFoundException("Protein not found");

            session.merge(protein);
            session.getTransaction().commit();
            return protein;
        }
    }

    @Override
    public boolean deleteProteinById(long id) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Protein protein = session.get(Protein.class, id);
            if (protein == null)
                throw new ProteinNotFoundException("Protein not found");

            session.remove(protein);
            session.getTransaction().commit();
            return true;
        }
    }
}
