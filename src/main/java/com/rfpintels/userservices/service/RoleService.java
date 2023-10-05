package com.rfpintels.userservices.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rfpintels.userservices.model.Role;
import com.rfpintels.userservices.model.RoleName;
import com.rfpintels.userservices.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Collection<Role> findAll() {
		return roleRepository.findAll();
	}

	public Role findByRoleName(RoleName roleName) {
		Role role = roleRepository.findByRoleName(roleName.toString());
		return role;
	}
}
