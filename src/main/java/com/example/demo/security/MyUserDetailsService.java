package com.example.demo.security;



import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Zavoláme repository, která vrací Optional<User>
        // 2. Pokud tam uživatel je, .orElseThrow ho "vytáhne" z krabičky
        // 3. Pokud tam není, vyhodí to chybu, kterou Spring Security zpracuje
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel s jménem " + username + " nebyl nalezen."));

        // 4. Vrátíme UserPrincipal (toho našeho "překladatele")
        return new UserPrincipal(user);
    }
}