package com.example.pal.service;

import com.example.pal.dto.CreateUserDTO;
import com.example.pal.dto.UserDTO;
import com.example.pal.model.Role;
import com.example.pal.model.User;
import com.example.pal.repository.RoleRepository;
import com.example.pal.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO createUserWithRoles(CreateUserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoles()) {
            Optional<Role> roleOpt = roleRepository.findByName(roleName);
            Role role = roleOpt.orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                return roleRepository.save(newRole);
            });
            roles.add(role);
        }

        user.setRoles(roles);
        return modelMapper.map(userRepository.save(user), UserDTO.class);}
    
    public List<UserDTO> getAllUsers() {
    	List<User> users = userRepository.findAll();
    	return users.stream().map(user->modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());	
    }
    
    public Optional<UserDTO> getUserById(Long id){
    	return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
    }
    
    public UserDTO updateUser(Long id, CreateUserDTO userDetails) {
        User userData = modelMapper.map(userDetails, User.class);
    	User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
    	user.setUsername(userData.getUsername());
    	if(user.getPassword()!=null) {
    		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
    	}
    	user.setRoles(userData.getRoles());
    	User updatedUser= userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
    
    public void deleteUser(Long id) {
    	userRepository.deleteById(id);
    }
}