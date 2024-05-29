package com.w2m.spaceships.services;

import com.w2m.spaceships.dtos.RoleDto;
import com.w2m.spaceships.dtos.UserDto;
import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Role;
import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.RoleRepository;
import com.w2m.spaceships.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public User addUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Username already exists: ").append(userDto.getUsername());
            throw new ResourceNotFoundException(messageBuilder.toString());
        }
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = modelMapper.map(userDto, User.class);

            Set<Role> roles = new HashSet<>();
            for (RoleDto roleDto : userDto.getRoles()) {
                Optional<Role> role = roleRepository.findByName(roleDto.getName());
                if (!role.isPresent()) {
                    StringBuilder messageBuilder = new StringBuilder();
                    messageBuilder.append("Role not found: ").append(roleDto.getName());
                    throw new ResourceNotFoundException(messageBuilder.toString());
                }
                roles.add(role.get());
            }
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (DataAccessException ex) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Error creating user: ").append(ex);
            throw new ResourceNotFoundException(messageBuilder.toString());
        }
    }
}
