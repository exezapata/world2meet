package com.w2m.spaceships.services;

import com.w2m.spaceships.dtos.SpaceshipDto;
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
    public Page<SpaceshipDto> findAll(Pageable pageable){
        Page<Spaceship> spaceshipPage = spaceshipRepository.findAll(pageable);
        if (spaceshipPage.isEmpty()) {
            throw new ResourceNotFoundException("No spaceships found");
        }
        return spaceshipPage.map(spaceship -> modelMapper.map(spaceship, SpaceshipDto.class));
    }

    public SpaceshipDto findById(Long id){
        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));
        return modelMapper.map(spaceship, SpaceshipDto.class);
    }

    @Cacheable(value = "spaceships-cache", key = "'spaceships-by-name' + #name")
    public Page<SpaceshipDto> findByNameContainingIgnoreCase(String name, Pageable pageable){
        Page<Spaceship> spaceshipsPage = spaceshipRepository.findByNameContainingIgnoreCase(name, pageable);
        if (spaceshipsPage.isEmpty()) {
            throw new ResourceNotFoundException("No spaceships found");
        }
        return spaceshipsPage.map(spaceship -> modelMapper.map(spaceship, SpaceshipDto.class));
    }

    public SpaceshipDto createSpaceship(SpaceshipDto spaceshipDto){
        Spaceship spaceship = modelMapper.map(spaceshipDto, Spaceship.class);
        Spaceship savedSpaceship = spaceshipRepository.save(spaceship);
        return modelMapper.map(savedSpaceship, SpaceshipDto.class);
    }

    public SpaceshipDto updateSpaceship(Long id, SpaceshipDto spaceshipDto){
        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));

        spaceship.setName(spaceshipDto.getName());
        spaceship.setSeries(spaceshipDto.getSeries());

        spaceship = spaceshipRepository.save(spaceship);

        return modelMapper.map(spaceship, SpaceshipDto.class);
    }

    @CacheEvict(value = "spaceships-cache", key = "'spaceship-' + #id")
    public void deleteSpaceship(Long id) {

        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spaceship not found with ID: " + id));

        spaceshipRepository.delete(spaceship);
    }

}
