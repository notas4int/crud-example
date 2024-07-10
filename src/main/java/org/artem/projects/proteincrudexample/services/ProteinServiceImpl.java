package org.artem.projects.proteincrudexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.repositories.ProteinRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProteinServiceImpl implements ProteinService {
    private final ProteinRepository proteinRepository;

    @Cacheable(cacheNames = "proteins", key = "#id")
    public Protein findProteinById(long id) {
        log.info("Doesn't exist in cache");
        return proteinRepository.getProtein(id);
    }

    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public int saveProtein(Protein protein) {
        return proteinRepository.createProtein(protein);
    }

    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public int updateProtein(Protein protein) {
        return proteinRepository.updateProtein(protein);
    }

    @CacheEvict(cacheNames = "proteins", key = "#id")
    public int deleteProteinById(long id) {
        return proteinRepository.deleteProtein(id);
    }
}
