package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.model.Role;
import mate.academy.repository.role.RoleRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getRoleByName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(()
                -> new RuntimeException("Can't find role by name"));
    }
}
