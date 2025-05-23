package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.bll.services.UserService;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<User> findByJobTitle(Pageable pageable, String jobTitle) {
        return userRepository.findByJobTitle(pageable, jobTitle);
    }

    public Page<User> findAvailableUsers(Pageable pageable) {
        return userRepository.findByAvailableTrue(pageable);
    }

    public boolean setUserAvailable(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setAvailable(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
