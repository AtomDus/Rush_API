package be.bdus.rush_api.bll.services;

import be.bdus.rush_api.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Page<User> findByJobTitle(Pageable pageable, String jobTitle);
}
