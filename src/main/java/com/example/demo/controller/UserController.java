

package com.example.demo.controller; // Uprav podle svého balíčku

import com.example.demo.entity.TaskList;
import com.example.demo.entity.User; // Uprav podle své entity User
import com.example.demo.repository.TaskListRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   @Autowired
    private PasswordEncoder passwordEncoder; 

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
    
    if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
    
        return ResponseEntity.badRequest().body("Uživatel s jménem '" + newUser.getUsername() + "' již existuje.");
    }

    if (newUser.getPassword() == null || newUser.getPassword().length() < 6) {
        return ResponseEntity.badRequest().body("Heslo musí mít alespoň 6 znaků!");
    }

    String encodedPassword = passwordEncoder.encode(newUser.getPassword());
    newUser.setPassword(encodedPassword);

    User savedUser = userRepository.save(newUser);

    
        TaskList newList = new TaskList();
        newList.setUser(savedUser); // Tady propojíme seznam s právě vytvořenou Petrou
        
        // UUID se vygeneruje samo díky tvému konstruktoru v entitě TaskList
        taskListRepository.save(newList);
    return ResponseEntity.ok(savedUser);
    }
}
