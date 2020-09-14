package by.dormitory.service;

import by.dormitory.entity.User;
import by.dormitory.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User getUserById(Integer id){
        return this.userRepository.findById(id).get();
    }

    public User getUserByDomain(String domain){
        return this.userRepository.findUserByDomain(domain);
    }
}