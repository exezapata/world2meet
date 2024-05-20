package com.w2m.spaceships.services;

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
    public void testFindAll_Returns_Results() {

        Page<Spaceship> spaceshipPage = new PageImpl<>(spaceshipList, pageable, spaceshipList.size());

        when(spaceshipRepository.findAll(pageable)).thenReturn(spaceshipPage);

        Page<Spaceship> result = spaceshipService.findAll(pageable);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(spaceshipList.size(), result.getContent().size());
        assertEquals("Millennium Falcon", result.getContent().get(0).getName());
    }

    @Test
    public void testFindAll_Returns_Empty() {

        Page<Spaceship> emptyPage = Page.empty(pageable);

        when(spaceshipRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Spaceship> result = spaceshipService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindById_Success() {
        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(spaceship));

        Spaceship result = spaceshipService.findById(id);

        assertNotNull(result);
        assertEquals(spaceship.getName(), result.getName());
    }

    @Test
    public void testFindById_ThrowsException() {

        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.findById(id);
        });

        assertTrue(exception.getMessage().contains("Spaceship not found with ID: " + id));
    }

    @Test
    public void testFindByNameContainingIgnoreCase_Returns_Results() {

        String name = "Serenity";

        Page<Spaceship> spaceshipPage = new PageImpl<>(spaceshipList, pageable, spaceshipList.size());

        when(spaceshipRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(spaceshipPage);

        Page<Spaceship> result = spaceshipService.findByNameContainingIgnoreCase(name, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.getTotalElements());
        assertEquals("Serenity", result.getContent().get(1).getName());
    }

    @Test
    public void testFindByNameContainingIgnoreCase_Returns_Empty() {

        String name = "NonExistentName";

        List<Spaceship> spaceshipList = new ArrayList<>();

        Page<Spaceship> emptyPage = new PageImpl<>(spaceshipList, pageable, spaceshipList.size());

        when(spaceshipRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(emptyPage);

        Page<Spaceship> result = spaceshipService.findByNameContainingIgnoreCase(name, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateSpaceship_Success() {

        com.w2m.spaceships.dtos.SpaceshipDto spaceshipDto = com.w2m.spaceships.dtos.SpaceshipDto.builder()
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        Spaceship spaceship = new Spaceship().builder()
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        when(modelMapper.map(spaceshipDto, Spaceship.class)).thenReturn(spaceship);

        when(spaceshipRepository.save(spaceship)).thenReturn(spaceship);

        Spaceship result = spaceshipService.createSpaceship(spaceshipDto);

        assertNotNull(result);
        assertEquals("USS Enterprise (NCC-1701)", result.getName());
        assertEquals("Star Trek", result.getSeries());
    }

    @Test
    public void testUpdateSpaceship_Success() {

        Long id = 1L;
        com.w2m.spaceships.dtos.SpaceshipDto spaceshipDto = com.w2m.spaceships.dtos.SpaceshipDto.builder()
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();

        Spaceship existingSpaceship = new Spaceship().builder()
                .id(id)
                .name("USS Enterprise")
                .series("Star Trek")
                .build();

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(existingSpaceship));

        when(spaceshipRepository.save(any(Spaceship.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Spaceship result = spaceshipService.updateSpaceship(id, spaceshipDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("USS Enterprise (NCC-1701)", result.getName());
        assertEquals("Star Trek", result.getSeries());
    }

    @Test
    void testDeleteSpaceship_Success() {

        Long id = 1L;

        when(spaceshipRepository.findById(id)).thenReturn(Optional.of(spaceship));

        doNothing().when(spaceshipRepository).delete(spaceship);

        spaceshipService.deleteSpaceship(id);

        verify(spaceshipRepository, times(1)).delete(spaceship);
    }



    //solo se limita al contexto del test
    @Import(SpaceshipService.class)
    static class TestConfig {
    }
}