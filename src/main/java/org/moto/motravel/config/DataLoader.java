package org.moto.motravel.config;

import org.moto.motravel.model.User;
import org.moto.motravel.model.Vehicle;
import org.moto.motravel.repository.UserRepository;
import org.moto.motravel.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Load test data only if repositories are empty
        if (userRepository.count() == 0) {
            loadUsers();
        }

        if (vehicleRepository.count() == 0) {
            loadVehicles();
        }
    }

    private void loadUsers() {
        System.out.println("Loading test users...");

        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@motravel.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(new HashSet<>(Arrays.asList("ROLE_ADMIN", "ROLE_USER")));
        userRepository.save(admin);

        // Create regular user
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@motravel.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(new HashSet<>(Arrays.asList("ROLE_USER")));
        userRepository.save(user);

        System.out.println("Test users loaded successfully!");
    }

    private void loadVehicles() {
        System.out.println("Loading test vehicles...");

        // Create test vehicles
        // Vehicle 1 - Car
        Vehicle car1 = new Vehicle();
        car1.setModel("Model 3");
        car1.setBrand("Tesla");
        car1.setType("car");
        car1.setLatitude(28.6139);  // Delhi coordinates
        car1.setLongitude(77.2090);
        car1.setHourlyPrice(25.0);
        car1.setImageUrl("https://example.com/tesla-model3.jpg");
        car1.setAvailability(true);
        vehicleRepository.save(car1);

        // Vehicle 2 - Car
        Vehicle car2 = new Vehicle();
        car2.setModel("Civic");
        car2.setBrand("Honda");
        car2.setType("car");
        car2.setLatitude(28.6129);  // Near Delhi
        car2.setLongitude(77.2295);
        car2.setHourlyPrice(15.0);
        car2.setImageUrl("https://example.com/honda-civic.jpg");
        car2.setAvailability(true);
        vehicleRepository.save(car2);

        // Vehicle 3 - Bike
        Vehicle bike1 = new Vehicle();
        bike1.setModel("Street 750");
        bike1.setBrand("Harley-Davidson");
        bike1.setType("bike");
        bike1.setLatitude(28.6219);  // Near Delhi
        bike1.setLongitude(77.2190);
        bike1.setHourlyPrice(10.0);
        bike1.setImageUrl("https://example.com/harley-street750.jpg");
        bike1.setAvailability(true);
        vehicleRepository.save(bike1);

        // Vehicle 4 - Bike
        Vehicle bike2 = new Vehicle();
        bike2.setModel("Ninja 650");
        bike2.setBrand("Kawasaki");
        bike2.setType("bike");
        bike2.setLatitude(28.6339);  // Near Delhi
        bike2.setLongitude(77.2290);
        bike2.setHourlyPrice(12.0);
        bike2.setImageUrl("https://example.com/kawasaki-ninja650.jpg");
        bike2.setAvailability(true);
        vehicleRepository.save(bike2);

        // Vehicle 5 - Car (not available)
        Vehicle car3 = new Vehicle();
        car3.setModel("X5");
        car3.setBrand("BMW");
        car3.setType("car");
        car3.setLatitude(28.6239);  // Near Delhi
        car3.setLongitude(77.2390);
        car3.setHourlyPrice(30.0);
        car3.setImageUrl("https://example.com/bmw-x5.jpg");
        car3.setAvailability(false);
        vehicleRepository.save(car3);

        System.out.println("Test vehicles loaded successfully!");
    }
}
