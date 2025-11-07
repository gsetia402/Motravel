package org.moto.motravel.config;

import org.moto.motravel.model.User;
import org.moto.motravel.model.Vendor;
import org.moto.motravel.repository.UserRepository;
import org.moto.motravel.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile({"default"})
public class StartupSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private VendorRepository vendorRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Admin
        userRepository.findByUsername("admin").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@motravel.local");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ROLE_ADMIN"));
            return userRepository.save(admin);
        });

        // Default Vendor + Vendor User
        Vendor vendor = vendorRepository.findAll().stream()
                .filter(v -> "Default Vendor".equalsIgnoreCase(v.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setName("Default Vendor");
                    v.setEmail("vendor@motravel.local");
                    v.setContactPhone("+");
                    v.setStatus("ACTIVE");
                    return vendorRepository.save(v);
                });

        userRepository.findByUsername("vendor").orElseGet(() -> {
            User u = new User();
            u.setUsername("vendor");
            u.setEmail("vendor@motravel.local");
            u.setPassword(passwordEncoder.encode("vendor123"));
            u.setRoles(Set.of("ROLE_VENDOR"));
            u.setVendorId(vendor.getId());
            return userRepository.save(u);
        });
    }
}
