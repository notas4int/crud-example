package org.artem.projects.proteincrudexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.artem.projects.proteincrudexample.daos.ProteinDao;
import org.artem.projects.proteincrudexample.dtos.ProteinCreatingDTO;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@RequiredArgsConstructor
public class ProteinServiceImpl implements ProteinService {
    private final ProteinDao proteinDao;

//    @Cacheable(cacheNames = "proteins", key = "#id")
    public Protein findProteinById(long id) {
        return proteinDao.findProteinById(id);
    }

//    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public Protein saveProtein(ProteinCreatingDTO proteinCreatingDTO) {
        Protein protein = Protein.builder()
                .name(proteinCreatingDTO.name())
                .brand(proteinCreatingDTO.brand())
                .cost(proteinCreatingDTO.cost())
                .build();
        return proteinDao.saveProtein(protein);
    }

//    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public Protein updateProtein(Protein protein) {
        return proteinDao.updateProtein(protein);
    }

//    @CacheEvict(cacheNames = "proteins", key = "#id")
    public boolean deleteProteinById(long id) {
        return proteinDao.deleteProteinById(id);
    }
}

