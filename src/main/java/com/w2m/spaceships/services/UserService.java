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
            throw new ResourceNotFoundException("Username already exists: " + userDto.getUsername());
        }
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = modelMapper.map(userDto, User.class);

            Set<Role> roles = new HashSet<>();
            for (RoleDto roleDto : userDto.getRoles()) {
                Optional<Role> role = roleRepository.findByName(roleDto.getName());
                if (!role.isPresent()) {
                    throw new ResourceNotFoundException("Role not found: " + roleDto.getName());
                }
                roles.add(role.get());
            }
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (DataAccessException ex) {
            throw new ResourceNotFoundException("Error creating user" + ex);
        }
    }
}
