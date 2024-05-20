package com.w2m.spaceships.services;

import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Spaceship;
import com.w2m.spaceships.repositories.SpaceshipRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceshipService {
    private final SpaceshipRepository spaceshipRepository;
    private final ModelMapper modelMapper;

    @Cacheable(value = "spaceships-cache", key = "'spaceships-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Spaceship> findAll(Pageable pageable){
        return spaceshipRepository.findAll(pageable);
    }

    @Cacheable(value = "spaceships-cache", key = "'spaceship-' + #id")
    public Spaceship findById(Long id){
        return spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));
    }

    @Cacheable(value = "spaceships-cache", key = "'spaceships-by-name' + #name")
    public Page<Spaceship> findByNameContainingIgnoreCase(String name, Pageable pageable){
        return spaceshipRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Spaceship createSpaceship(com.w2m.spaceships.dtos.SpaceshipDto spaceshipDto){
        Spaceship spaceship = modelMapper.map(spaceshipDto, Spaceship.class);
        return spaceshipRepository.save(spaceship);
    }

    public Spaceship updateSpaceship(Long id, com.w2m.spaceships.dtos.SpaceshipDto spaceshipDto){
        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));

        spaceship.setName(spaceshipDto.getName());
        spaceship.setSeries(spaceshipDto.getSeries());

        return spaceshipRepository.save(spaceship);
    }

    @CacheEvict(value = "spaceships-cache", key = "'spaceship-' + #id")
    public void deleteSpaceship(Long id) {

        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));

        spaceshipRepository.delete(spaceship);
    }

}
