package com.w2m.spaceships.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Spaceship;
import com.w2m.spaceships.repositories.SpaceshipRepository;
import com.w2m.spaceships.services.JwtService;
import com.w2m.spaceships.services.SpaceshipService;
import com.w2m.spaceships.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@WebMvcTest(SpaceshipController.class)
@ActiveProfiles("test")
@WithMockUser
class SpaceshipControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SpaceshipController spaceshipController;
    @MockBean
    private SpaceshipService spaceshipService;

    @MockBean
    private JwtService JwtTokenUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private SpaceshipRepository spaceshipRepository;

    private Spaceship spaceship;
    private List<Spaceship> spaceshipList;
    private Pageable pageable;
    private ObjectMapper objectMapper;

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
    public void testGetAllSpaceships_Returns_Empty() throws Exception {

        Page<Spaceship> spaceshipsPage = new PageImpl<>(List.of(new Spaceship(), new Spaceship()));
        when(spaceshipService.findAll(pageable)).thenReturn(spaceshipsPage);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/spaceship/spaceships")
                        .header("Authorization", "Bearer your-token")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    public void testGetAllSpaceships_Returns_Results() throws Exception {

        Page<Spaceship> spaceshipsPage = new PageImpl<>(spaceshipList, pageable, spaceshipList.size());

        when(spaceshipService.findAll(pageable)).thenReturn(spaceshipsPage);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/spaceship/spaceships")
                        .header("Authorization", "Bearer your-token")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        objectMapper = new ObjectMapper();
        JsonNode jsonContent = objectMapper.readTree(response.getContentAsString());
        int result = jsonContent.get("content").size();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals(3, result);
    }



    @Test
    public void testGetSpaceshipById_Returns_Results() {

        Long id = 1L;

        Spaceship expectedSpaceship = spaceship;

        when(spaceshipService.findById(id)).thenReturn(expectedSpaceship);

        Spaceship response = spaceshipController.getSpaceshipById(id);

        assertEquals(expectedSpaceship, response);

    }

    @Test
    public void testGetSpaceshipById_NotFound() throws Exception {

        Long spaceshipId = 2555L;

        when(spaceshipService.findById(spaceshipId)).thenThrow(new ResourceNotFoundException("Spaceship not found with ID: " + spaceshipId));

        mockMvc.perform(get("/api/spaceship/{id}", spaceshipId)
                        .header("Authorization", "Bearer your-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Spaceship not found with ID: " + spaceshipId));
    }

    @Test
    public void testSearchSpaceshipsByName_Returns_Empty() throws Exception {

        final String name = "Discovery";
        Page<Spaceship> spaceshipsPage = new PageImpl<>(new ArrayList<>());
        when(spaceshipService.findByNameContainingIgnoreCase(name, pageable)).thenReturn(spaceshipsPage);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/spaceship/search")
                        .header("Authorization", "Bearer your-token")
                        .param("name", name)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        objectMapper = new ObjectMapper();
        JsonNode jsonContent = objectMapper.readTree(response.getContentAsString());
        int result = jsonContent.get("content").size();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals(0, result);
    }

    @Test
    public void testSearchSpaceshipsByName_Returns_Results() throws Exception {

        final String name = "Eagle";
        Page<Spaceship> spaceshipsPage = new PageImpl<>(spaceshipList);
        when(spaceshipService.findByNameContainingIgnoreCase(name, pageable)).thenReturn(spaceshipsPage);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/spaceship/search")
                        .header("Authorization", "Bearer your-token")
                        .param("name", name)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        objectMapper = new ObjectMapper();
        JsonNode jsonContent = objectMapper.readTree(response.getContentAsString());
        JsonNode result = jsonContent.get("content").get(2);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("Eagle 5", result.get("name").asText());

    }

    @Test
    public void testCreateSpaceship_Success() {

        com.w2m.spaceships.dtos.SpaceshipDto spaceshipDto = new com.w2m.spaceships.dtos.SpaceshipDto();
        spaceshipDto.setName("Discovery One");
        spaceshipDto.setSeries("A Space Odyssey");

        when(spaceshipService.createSpaceship(any(com.w2m.spaceships.dtos.SpaceshipDto.class))).thenReturn(this.spaceship);

        Spaceship createdSpaceship = spaceshipController.createSpaceship(spaceshipDto);

        assertNotNull(createdSpaceship);
        assertEquals("Discovery One", createdSpaceship.getName());
        assertEquals("A Space Odyssey", createdSpaceship.getSeries());
    }

    @Test
    public void testDeleteSpaceship_Success() {

        Long id = 1L;

        when(spaceshipService.findById(id)).thenReturn(spaceship);

        assertDoesNotThrow(() -> spaceshipController.deleteSpaceship(id));
    }
}