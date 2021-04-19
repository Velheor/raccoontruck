package com.velheor.internship.mappers;

import com.velheor.internship.dto.RoleViewDTO;
import com.velheor.internship.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleViewDTO roleToRoleDto(Role role);

    Role roleDtoToRole(RoleViewDTO role);
}