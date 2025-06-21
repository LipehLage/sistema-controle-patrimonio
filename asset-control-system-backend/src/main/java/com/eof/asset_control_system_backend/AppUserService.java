package com.eof.asset_control_system_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser saveUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> updateUser(Long id, AppUser userDetails) {
        Optional<AppUser> userOptional = appUserRepository.findById(id);
        if (userOptional.isPresent()) {
            AppUser existingUser = userOptional.get();
            existingUser.setName(userDetails.getName());
            existingUser.setDepartment(userDetails.getDepartment());
            existingUser.setEmail(userDetails.getEmail());
            return Optional.of(appUserRepository.save(existingUser));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        if (appUserRepository.existsById(id)) {
            appUserRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}