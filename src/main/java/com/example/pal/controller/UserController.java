package com.example.pal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pal.dto.CreateUserDTO;
import com.example.pal.dto.UserDTO;
import com.example.pal.model.User;
import com.example.pal.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO userDTO){ 
        UserDTO userDto = userService.createUserWithRoles(userDTO);
        return ResponseEntity.status(201).body(userDto);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
    	return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
    	Optional<UserDTO> user = userService.getUserById(id);
    	return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody CreateUserDTO userDetails){
    	UserDTO updatedUser = userService.updateUser(id, userDetails);
    	return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
    	userService.deleteUser(id);
    	return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam String roleName) {
    	List<User> users = userService.getUsersByRole(roleName);
    	if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(users);
    }    
}
