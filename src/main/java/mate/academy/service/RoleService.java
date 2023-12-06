package mate.academy.service;

import mate.academy.model.Role;

public interface RoleService {
    Role getRoleByName(Role.RoleName roleName);
}
