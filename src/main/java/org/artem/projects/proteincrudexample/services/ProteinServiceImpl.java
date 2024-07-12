package org.artem.projects.proteincrudexample.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.artem.projects.proteincrudexample.entities.Protein;
import org.artem.projects.proteincrudexample.exceptions.ProteinNotFoundException;
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
        return proteinRepository.findById(id).orElseThrow(() -> new ProteinNotFoundException("Protein not found"));
    }

    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public Protein saveProtein(Protein protein) {
        return proteinRepository.save(protein);
    }

    @CachePut(cacheNames = "proteins", key = "#protein.id")
    public Protein updateProtein(Protein protein) {
        if (!proteinRepository.existsById(protein.getId()))
            throw new ProteinNotFoundException("Protein not found");

        return proteinRepository.save(protein);
    }

    @CacheEvict(cacheNames = "proteins", key = "#id")
    public boolean deleteProteinById(long id) {
        if (!proteinRepository.existsById(id))
            throw new ProteinNotFoundException("Protein not found");

        proteinRepository.deleteById(id);
        return true;
    }
}
