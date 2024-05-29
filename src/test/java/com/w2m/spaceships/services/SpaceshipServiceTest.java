package com.w2m.spaceships.services;

import com.w2m.spaceships.dtos.SpaceshipDto;
import com.w2m.spaceships.dtos.SpaceshipRequestDto;
import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Spaceship;
import com.w2m.spaceships.repositories.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = SpaceshipServiceTest.TestConfig.class)
class SpaceshipServiceTest {

    @Autowired
    private SpaceshipService spaceshipService;
    @MockBean
    private SpaceshipRepository spaceshipRepository;

    @MockBean
    private CacheManager cacheManager;
    @MockBean
    private ModelMapper modelMapper;

    private Spaceship spaceship;
    private List<Spaceship> spaceshipList;
    private Pageable pageable;


    @BeforeEach
    void setUp() {
        spaceship = new Spaceship(1L, "Discovery One", "A Space Odyssey");

        spaceshipList = new ArrayList<>();
        spaceshipList.add(new Spaceship(2L, "Millennium Falcon", "Star Wars"));
        spaceshipList.add(new Spaceship(3L, "Serenity", "Firefly"));
        spaceshipList.add(new Spaceship(4L, "Eagle 5", "Spaceballs"));

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindAll_Returns_Results() {

        List<SpaceshipDto> spaceshipDtoList = spaceshipList.stream()
                .map(spaceship -> modelMapper.map(spaceship, SpaceshipDto.class))
                .collect(Collectors.toList());

        Page<SpaceshipDto> spaceshipDtoPage = new PageImpl<>(spaceshipDtoList, pageable, spaceshipDtoList.size());

        when(spaceshipRepository.findAll(pageable)).thenReturn(new PageImpl<>(spaceshipList, pageable, spaceshipList.size()));

        when(modelMapper.map(any(Spaceship.class), any()))
                .thenAnswer(invocation -> {
                    Spaceship source = invocation.getArgument(0);
                    return new SpaceshipDto(source.getId(), source.getName(), source.getSeries());
                });

        Page<SpaceshipDto> result = spaceshipService.findAll(pageable);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(spaceshipList.size(), result.getContent().size());
        assertEquals("Millennium Falcon", result.getContent().get(0).getName());

    }

    @Test
    void testFindAll_ThrowsException() {

        Page<Spaceship> emptyPage = Page.empty(pageable);

        when(spaceshipRepository.findAll(pageable)).thenReturn(emptyPage);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.findAll(pageable);
        });

        assertEquals("No spaceships found", thrown.getMessage());
    }

    @Test
    void testFindById_Success() {
        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(spaceship));

        SpaceshipDto expectedDto = SpaceshipDto.builder()
                .id(spaceship.getId())
                .name(spaceship.getName())
                .series(spaceship.getSeries())
                .build();

        when(modelMapper.map(spaceship, SpaceshipDto.class)).thenReturn(expectedDto);

        SpaceshipDto result = spaceshipService.findById(id);

        assertNotNull(result);
        assertEquals(spaceship.getName(), result.getName());
        verify(spaceshipRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).map(spaceship, SpaceshipDto.class);

    }

    @Test
    void testFindById_ThrowsException() {

        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.findById(id);
        });

        assertEquals("Spaceship not found with ID: " + id, thrown.getMessage());

    }

    @Test
    void testFindByNameContainingIgnoreCase_ThrowsException() {

        String name = "Serenity";

        Page<Spaceship> emptyPage = Page.empty(pageable);

        when(spaceshipRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(emptyPage);

        when(modelMapper.map(any(), any())).thenReturn(new SpaceshipDto());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.findByNameContainingIgnoreCase(name, pageable);
        });

        assertEquals("No spaceships found", thrown.getMessage());

    }

    @Test
    void testCreateSpaceship_Success() {

        SpaceshipRequestDto spaceshipRequestDto = SpaceshipRequestDto.builder()
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        SpaceshipDto spaceshipDto = new SpaceshipDto().builder()
                .id(1L)
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        Spaceship spaceship = new Spaceship().builder()
                .id(1L)
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        when(modelMapper.map(spaceshipRequestDto, Spaceship.class)).thenReturn(spaceship);

        when(modelMapper.map(spaceship, SpaceshipDto.class)).thenReturn(spaceshipDto);

        when(spaceshipRepository.save(any(Spaceship.class))).thenReturn(spaceship);

        SpaceshipDto result = spaceshipService.createSpaceship(spaceshipRequestDto);

        assertNotNull(result);
        assertEquals("USS Enterprise (NCC-1701)", result.getName());
        assertEquals("Star Trek", result.getSeries());

    }

    @Test
    void testUpdateSpaceship_Success() {

        Long id = 1L;

        SpaceshipRequestDto updateDto = SpaceshipRequestDto.builder()
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        Spaceship existingSpaceship = new Spaceship().builder()
                .id(id)
                .name("USS Enterprise")
                .series("Star Trek")
                .build();

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(existingSpaceship));

        when(spaceshipRepository.save(any(Spaceship.class))).thenReturn(existingSpaceship);

        SpaceshipDto mappedSpaceshipDto = new SpaceshipDto(id, updateDto.getName(), updateDto.getSeries());

        when(modelMapper.map(existingSpaceship, SpaceshipDto.class)).thenReturn(mappedSpaceshipDto);

        SpaceshipDto result = spaceshipService.updateSpaceship(id, updateDto);

        assertNotNull(result);
        assertEquals("USS Enterprise (NCC-1701)", result.getName());
        assertEquals("Star Trek", result.getSeries());

        verify(spaceshipRepository, times(1)).findById(id);
        verify(spaceshipRepository, times(1)).save(any(Spaceship.class));

    }

    @Test
    void testDeleteSpaceship_Success() {

        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(spaceship));

        doNothing().when(spaceshipRepository).delete(spaceship);

        spaceshipService.deleteSpaceship(id);

        verify(spaceshipRepository, times(1)).delete(spaceship);
    }


    @Import(SpaceshipService.class)
    static class TestConfig {
    }
}