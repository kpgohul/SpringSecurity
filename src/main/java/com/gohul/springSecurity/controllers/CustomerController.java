package com.gohul.springSecurity.controllers;

import com.gohul.springSecurity.constants.AppConstants;
import com.gohul.springSecurity.customer.*;
import com.gohul.springSecurity.login.LoginRequest;
import com.gohul.springSecurity.login.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerRepo repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;
    private final RolesRepo roleRepo;
    private static final SecureRandom random = new SecureRandom();

    @PostMapping("/register")
    public ResponseEntity<String> getRegister(@RequestBody CustomerPlusRolesRequest request) {
        log.info("Received customer responces ---> {}", request);
        try {
            if (repo.findByUsername(request.username()).isPresent())
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body("username:: " + request.username() + " already exist!");
            if (repo.findByMobileNumber(request.mobileNumber()).isPresent())
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body("mobile number:: " + request.mobileNumber() + " already exist!");
            log.info("the Given password:: {}", request.password());
            String hashCodedPwd = encoder.encode(request.password());
            Customer customer = toCustomer(request);
            Long cusID = generateUniqueCustomerNumber();
            customer.setCustomerId(cusID);
            customer.setPassword(hashCodedPwd);
            System.out.println("<----------------------------{" + customer + "}----------------------------->");

            Customer savedCustomer = repo.save(customer);
            for (String role : request.roles()) {
                CustomerRoles cusRole = CustomerRoles.builder()
                        .roleId(generateUniqueRoleNumber())
                        .customer(customer)
                        .role(role)
                        .build();
                roleRepo.save(cusRole);
            }
            if (savedCustomer.getCustomerId() > 0)
                return ResponseEntity.status(HttpStatus.CREATED).body("Given user details got created!");
            else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred::" + e.getMessage());
        }

//            return ResponseEntity.ok("Testing........");
//        }catch (Exception e){return ResponseEntity.ok("Testing........");}
    }

    @GetMapping("/customer")
    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = repo.findByEmail(authentication.getName());
        if (optionalCustomer.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found:: " + authentication.getName());
        else return ResponseEntity.ok(toCustomerResponse(optionalCustomer.get()));
    }


    private Long generateUniqueCustomerNumber() {
        long number;
        do {
            number = 1000000000000000L + (Math.abs(random.nextLong()) % 9000000000000000L);
        } while (repo.existsById(number)); // Retry if exists
        return number;
    }

    private Long generateUniqueRoleNumber() {
        long number;
        do {
            number = 10000000L + (Math.abs(random.nextLong()) % 90000000L);
        } while (roleRepo.existsByRoleId(number)); // Retry if exists
        return number;
    }

    private Customer toCustomer(CustomerPlusRolesRequest request) {
        return Customer.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .mobileNumber(request.mobileNumber())
                .build();
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        List<CustomerRoles> roles = roleRepo.findByCustomer(customer);
        return new CustomerResponse(customer.getCustomerId()
                , customer.getUsername()
                , customer.getEmail()
                , customer.getMobileNumber()
                , roles.stream()
                .map(CustomerRoles::getRole)
                .collect(Collectors.toList()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> apiLogin(@RequestBody LoginRequest request) {
        String jwt = "";
        try
        {
            Authentication authenticationBefore = UsernamePasswordAuthenticationToken
                    .unauthenticated(request.email(),request.password());
            Authentication authenticationAfter = authenticationManager.authenticate(authenticationBefore);
            if (authenticationAfter != null && authenticationAfter.isAuthenticated())
            {
                String originalSecretKey = environment.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_KEY_DEFAULT);
                SecretKey decodedSecretKey = Keys.hmacShaKeyFor(originalSecretKey.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("Gohul's First Application").subject("Jwt_Token")
                        .claim("username",authenticationAfter.getName())
                        .claim("authorities", authenticationAfter.getAuthorities()
                                .stream().map(auth -> auth.getAuthority())
                                .collect(Collectors.joining(",")))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date()).getTime()+30000000000L))
                        .signWith(decodedSecretKey)
                        .compact();

            }
            LoginResponse response = new LoginResponse(HttpStatus.OK.getReasonPhrase(),jwt);
            return ResponseEntity.status(HttpStatus.OK).header(AppConstants.JWT_HEADER,jwt)
                    .body(response);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Error","Login failed due to "+e));
        }


    }
}
