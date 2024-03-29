package com.service.todoit.domain.user.dto;

import com.service.todoit.domain.project.dto.ProjectDto;
import com.service.todoit.domain.project.dto.ProjectResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UserDto user;
    private List<ProjectResponseDto> projects;

}
