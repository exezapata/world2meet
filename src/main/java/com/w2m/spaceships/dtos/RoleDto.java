package com.w2m.spaceships.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {

    @NotBlank(message = "Id cannot be blank")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

}
