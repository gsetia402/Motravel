package org.moto.motravel.controller;

import jakarta.validation.Valid;
import org.moto.motravel.model.User;
import org.moto.motravel.model.Vendor;
import org.moto.motravel.payload.request.LoginRequest;
import org.moto.motravel.payload.request.SignupRequest;
import org.moto.motravel.payload.request.VendorSignupRequest;
import org.moto.motravel.payload.response.JwtResponse;
import org.moto.motravel.payload.response.MessageResponse;
import org.moto.motravel.repository.UserRepository;
import org.moto.motravel.repository.VendorRepository;
import org.moto.motravel.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        // Vendor approval gate: vendors can login only when APPROVED
        if (user.getRoles() != null && user.getRoles().contains("ROLE_VENDOR")) {
            if (user.getVendorId() == null) {
                return ResponseEntity.status(403).body(new MessageResponse("Vendor account not linked."));
            }
            var vendorOpt = vendorRepository.findById(user.getVendorId());
            if (vendorOpt.isEmpty()) {
                return ResponseEntity.status(403).body(new MessageResponse("Vendor not found."));
            }
            var vendor = vendorOpt.get();
            if (!"APPROVED".equalsIgnoreCase(vendor.getStatus())) {
                String msg = switch (vendor.getStatus() == null ? "PENDING" : vendor.getStatus().toUpperCase()) {
                    case "REJECTED" -> "Your vendor registration was rejected" + (vendor.getRejectionReason() != null ? ": " + vendor.getRejectionReason() : ".");
                    default -> "Your vendor registration is pending approval.";
                };
                return ResponseEntity.status(403).body(new MessageResponse(msg));
            }
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 user.getId(),
                                                 user.getUsername(),
                                                 user.getEmail(),
                                                 roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<String> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add("ROLE_USER");
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add("ROLE_ADMIN");
                        break;
                    default:
                        roles.add("ROLE_USER");
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/vendor-signup")
    public ResponseEntity<?> vendorSignup(@Valid @RequestBody VendorSignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create Vendor with PENDING status
        Vendor vendor = new Vendor();
        vendor.setName(request.getCompanyName());
        vendor.setCompanyName(request.getCompanyName());
        vendor.setEmail(request.getEmail());
        vendor.setContactPhone(request.getContactPhone());
        String dept = request.getDepartment() != null ? request.getDepartment().trim().toUpperCase() : "TOUR";
        if (!dept.equals("TOUR") && !dept.equals("VEHICLE")) dept = "TOUR";
        vendor.setDepartment(dept);
        vendor.setStatus("PENDING");
        vendor = vendorRepository.save(vendor);

        // Create vendor User linked to Vendor
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        java.util.Set<String> roles = new java.util.HashSet<>();
        roles.add("ROLE_VENDOR");
        user.setRoles(roles);
        user.setVendorId(vendor.getId());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Vendor registered successfully! Awaiting admin approval."));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return ResponseEntity.status(401).body(Map.of("error", "unauthenticated"));
        var principal = auth.getPrincipal();
        String username;
        if (principal instanceof org.springframework.security.core.userdetails.User u) {
            username = u.getUsername();
        } else {
            username = String.valueOf(principal);
        }
        var roles = auth.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        String vendorId = userRepository.findByUsername(username).map(org.moto.motravel.model.User::getVendorId).orElse(null);
        return ResponseEntity.ok(Map.of(
                "username", username,
                "authorities", roles,
                "vendorId", vendorId
        ));
    }
}
