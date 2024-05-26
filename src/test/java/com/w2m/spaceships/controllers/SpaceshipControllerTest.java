package com.w2m.spaceships.controllers;

import com.w2m.spaceships.dtos.SpaceshipDto;

import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.filters.JwtFilter;
import com.w2m.spaceships.repositories.SpaceshipRepository;

import com.w2m.spaceships.services.JwtService;
import com.w2m.spaceships.services.SpaceshipService;

import com.w2m.spaceships.services.UserDetailsServiceImpl;
import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;





@SpringBootTest
@ActiveProfiles("test")
class SpaceshipControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private SpaceshipController spaceshipController;
    @MockBean
    private SpaceshipService spaceshipService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private SpaceshipRepository spaceshipRepository;

    @MockBean
    private ModelMapper modelMapper;

    private  SpaceshipDto spaceshipDto1;
    private  SpaceshipDto spaceshipDto2;
    private int page = 0;
    private int size = 10;
    private String token = "Bearer test_token";


    @BeforeEach
    void setUp() throws ServletException, IOException {

        spaceshipDto1 = SpaceshipDto.builder()
                .id(1L)
                .name("Millennium Falcon")
                .series("Star Wars")
                .build();

        spaceshipDto2 = SpaceshipDto.builder()
                .id(4L)
                .name("USS Enterprise (NCC-1701)")
                .series("Star Trek")
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testGetAllSpaceships_Returns_Results() {

        Page<SpaceshipDto> spaceshipPage = new PageImpl<>(Arrays.asList(spaceshipDto1, spaceshipDto2), PageRequest.of(page, size), 2);

        when(spaceshipService.findAll(PageRequest.of(page, size))).thenReturn(spaceshipPage);

        Page<SpaceshipDto> result = spaceshipController.getAllSpaceships(token, page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(spaceshipDto1, result.getContent().get(0));

        verify(spaceshipService, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testGetAllSpaceships_Throws_ResourceNotFoundException() {

        when(spaceshipService.findAll(PageRequest.of(page, size))).thenThrow(new ResourceNotFoundException("No spaceships found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipController.getAllSpaceships(token, page, size);
        });

        assertEquals("No spaceships found", exception.getMessage());

        verify(spaceshipService, times(1)).findAll(PageRequest.of(page, size));

    }
    @Test
    void testGetAllSpaceships_WithoutAdminRole_ReturnsForbidden() throws Exception {

        UserDetails user = User.withUsername("user")
                .password("password")
                .roles("USER")
                .build();

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        TestSecurityContextHolder.setContext(context);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            spaceshipController.getAllSpaceships(token, page, size);
        });

        assertTrue(exception.getMessage().contains("Access Denied"));

        verify(spaceshipService, times(0)).findAll(PageRequest.of(page, size));

    }

    @Test
    void testGetAllSpaceships_WitAdminRole_ReturnsOk() throws Exception {

        UserDetails user = User.withUsername("user")
                .password("password")
                .roles("ADMIN")
                .build();

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
        TestSecurityContextHolder.setContext(context);

        Pageable pageable = PageRequest.of(page, size);

        when(spaceshipService.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        Page<SpaceshipDto> result = spaceshipController.getAllSpaceships("Bearer test_token", page, size);

        assertTrue(result.isEmpty());

        verify(spaceshipService, times(1)).findAll(pageable);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void testGetSpaceshipById_Returns_Result() {

        Long spaceshipId = 1L;

        when(spaceshipService.findById(spaceshipId)).thenReturn(spaceshipDto1);

        SpaceshipDto result = spaceshipController.getSpaceshipById(token, spaceshipId);

        assertNotNull(result);
        assertEquals(spaceshipDto1.getId(), result.getId());

        verify(spaceshipService, times(1)).findById(spaceshipId);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void testGetSpaceshipById_NotFound_ThrowsException() {

        Long spaceshipId = 1L;

        when(spaceshipService.findById(spaceshipId)).thenThrow(new ResourceNotFoundException("Spaceship not found with ID: " + spaceshipId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipController.getSpaceshipById(token, spaceshipId);
        });

        String expectedMessage = "Spaceship not found with ID: " + spaceshipId;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(spaceshipService, times(1)).findById(spaceshipId);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void testSearchSpaceshipsByName_Returns_Results() {

        String name = "Falcon";

        Pageable pageable = PageRequest.of(page, size);
        Page<SpaceshipDto> spaceshipPage = new PageImpl<>(Arrays.asList(spaceshipDto1, spaceshipDto2), pageable, 2);

        when(spaceshipService.findByNameContainingIgnoreCase(name, pageable)).thenReturn(spaceshipPage);

        Page<SpaceshipDto> result = spaceshipController.searchSpaceshipsByName(token, name, page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(spaceshipDto1.getName(), result.getContent().get(0).getName());
        assertEquals(spaceshipDto2.getName(), result.getContent().get(1).getName());

        verify(spaceshipService, times(1)).findByNameContainingIgnoreCase(name, pageable);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void testSearchSpaceshipsByName_NotFound_ThrowsException() {

        String name = "Unknown";

        Pageable pageable = PageRequest.of(page, size);

        when(spaceshipService.findByNameContainingIgnoreCase(name, pageable))
                .thenThrow(new ResourceNotFoundException("No spaceships found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipController.searchSpaceshipsByName(token, name, page, size);
        });

        String expectedMessage = "No spaceships found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(spaceshipService, times(1)).findByNameContainingIgnoreCase(name, pageable);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testCreateSpaceship_ReturnsCreatedSpaceship() {

        when(spaceshipService.createSpaceship(spaceshipDto1)).thenReturn(spaceshipDto1);

        SpaceshipDto result = spaceshipController.createSpaceship(token, spaceshipDto1);

        assertNotNull(result);
        assertEquals(spaceshipDto1.getName(), result.getName());
        assertEquals(spaceshipDto1.getSeries(), result.getSeries());

        verify(spaceshipService, times(1)).createSpaceship(spaceshipDto1);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testUpdateSpaceship_ReturnsUpdatedSpaceship() {

        Long spaceshipId = 1L;

        when(spaceshipService.updateSpaceship(spaceshipId, spaceshipDto1)).thenReturn(spaceshipDto1);

        SpaceshipDto result = spaceshipController.updateSpaceship(token, spaceshipId, spaceshipDto1);

        assertNotNull(result);
        assertEquals(spaceshipDto1.getName(), result.getName());
        assertEquals(spaceshipDto1.getSeries(), result.getSeries());

        verify(spaceshipService, times(1)).updateSpaceship(spaceshipId, spaceshipDto1);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testUpdateSpaceship_NotFound_ThrowsException() {

        Long spaceshipId = 1L;

        SpaceshipDto invalidSpaceshipDto = SpaceshipDto.builder()
                            .name(null)
                            .series(null)
                            .build();

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data"))
                .when(spaceshipService).updateSpaceship(spaceshipId, invalidSpaceshipDto);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            spaceshipController.updateSpaceship(token, spaceshipId, invalidSpaceshipDto);
        });

        String expectedMessage = "Invalid data";
        String actualMessage = exception.getReason();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(spaceshipService, times(1)).updateSpaceship(spaceshipId, invalidSpaceshipDto);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testDeleteSpaceship_DeletesSpaceship() {

        Long spaceshipId = 1L;

        doNothing().when(spaceshipService).deleteSpaceship(spaceshipId);

        spaceshipController.deleteSpaceship(token, spaceshipId);

        verify(spaceshipService, times(1)).deleteSpaceship(spaceshipId);
    }

}