package com.w2m.spaceships.controllers;

import com.w2m.spaceships.dtos.SpaceshipDto;
import com.w2m.spaceships.services.SpaceshipService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/spaceship")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceshipController {

    private final SpaceshipService spaceshipService;

    @Operation(summary = "Get all spaceships with pagination")
    @GetMapping("/spaceships")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<SpaceshipDto> getAllSpaceships(@RequestHeader(name="Authorization") String token,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return spaceshipService.findAll(pageable);
    }

    @Operation(summary = "Get a spaceship by ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public SpaceshipDto getSpaceshipById(@RequestHeader(name="Authorization") String token,
                                         @PathVariable Long id) {
        return spaceshipService.findById(id);
    }

    @Operation(summary = "Search for spaceships by name")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<SpaceshipDto> searchSpaceshipsByName(@RequestHeader(name="Authorization") String token,
                                                     @RequestParam String name,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return spaceshipService.findByNameContainingIgnoreCase(name, pageable);
    }

    @Operation(summary = "Create a new spaceship")
    @PostMapping("/spaceship")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SpaceshipDto createSpaceship(@RequestHeader(name="Authorization") String token,
                                        @Valid @RequestBody SpaceshipDto spaceshipDto) {
        return spaceshipService.createSpaceship(spaceshipDto);
    }

    @Operation(summary = "Update an existing spacecraft")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SpaceshipDto updateSpaceship(@RequestHeader(name="Authorization") String token,
                                        @PathVariable Long id,
                                        @Valid @RequestBody SpaceshipDto spaceshipDtoDetails) {
        return spaceshipService.updateSpaceship(id, spaceshipDtoDetails);
    }

    @Operation(summary = "Delete a spaceship")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSpaceship(@RequestHeader(name="Authorization") String token,
                                @PathVariable Long id) {
        spaceshipService.deleteSpaceship(id);
    }

}
