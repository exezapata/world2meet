package com.w2m.spaceships.initializations;

import com.w2m.spaceships.models.Role;
import com.w2m.spaceships.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.db.init", havingValue = "true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleInitialization {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() throws ParseException {
        initializeRole();
    }

    private void initializeRole() throws ParseException {

        Role roleAdmin = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .build();

        roleRepository.save(roleAdmin);

        Role roleUser = Role.builder()
                .id(2L)
                .name("ROLE_USER")
                .build();

        roleRepository.save(roleUser);
    }

}
