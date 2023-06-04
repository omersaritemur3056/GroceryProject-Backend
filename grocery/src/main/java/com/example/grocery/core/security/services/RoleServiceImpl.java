package com.example.grocery.core.security.services;

import com.example.grocery.core.security.DTOs.response.GetAllRoleResponseDto;
import com.example.grocery.core.security.DTOs.response.GetAllUserResponseDto;
import com.example.grocery.core.security.models.Role;
import com.example.grocery.core.security.repository.RoleRepository;
import com.example.grocery.core.security.services.constants.Messages;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final MapperService mapperService;

    @Override
    public DataResult<List<GetAllRoleResponseDto>> getAll() {
        List<Role> roleList = roleRepository.findAll();
        List<GetAllRoleResponseDto> returnList = roleList.stream()
                .map(r -> {
                    GetAllRoleResponseDto responseDto = new GetAllRoleResponseDto();
                    responseDto.setId(r.getId());
                    responseDto.setName(r.getName().toString());

                    return responseDto;
                }).toList();

        return new SuccessDataResult<>(returnList, Messages.GetListMessages.ROLES_LISTED);
    }
}
