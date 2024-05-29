package com.w2m.spaceships.controllers;

import com.w2m.spaceships.dtos.SpaceshipDto;
import com.w2m.spaceships.dtos.SpaceshipRequestDto;
import com.w2m.spaceships.services.SpaceshipService;
import com.w2m.spaceships.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @GetMapping("/spaceships")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<SpaceshipDto> getAllSpaceships(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return spaceshipService.findAll(pageable);
    }

    @Operation(summary = "Get a spaceship by ID")
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public SpaceshipDto getSpaceshipById(@PathVariable Long id) {
        return spaceshipService.findById(id);
    }

    @Operation(summary = "Search for spaceships by name")
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<SpaceshipDto> searchSpaceshipsByName(@RequestParam String name,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return spaceshipService.findByNameContainingIgnoreCase(name, pageable);
    }

    @Operation(summary = "Create a new spaceship")
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @PostMapping("/spaceship")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SpaceshipDto createSpaceship(@Valid @RequestBody SpaceshipRequestDto spaceshipDto) {
        return spaceshipService.createSpaceship(spaceshipDto);
    }

    @Operation(summary = "Update an existing spacecraft")
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SpaceshipDto updateSpaceship(@PathVariable Long id,
                                        @Valid @RequestBody SpaceshipRequestDto updateDto) {
        return spaceshipService.updateSpaceship(id, updateDto);
    }

    @Operation(summary = "Delete a spaceship")
    @SecurityRequirement(name = Constants.BEARER_JWT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSpaceship(@PathVariable Long id) {
        spaceshipService.deleteSpaceship(id);
    }

}
