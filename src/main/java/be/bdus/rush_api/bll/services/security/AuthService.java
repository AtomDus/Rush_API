package be.bdus.rush_api.bll.services.security;

import be.bdus.rush_api.dl.entities.User;

public interface AuthService {

    void register(User user);

    User login(String email, String password);
}
