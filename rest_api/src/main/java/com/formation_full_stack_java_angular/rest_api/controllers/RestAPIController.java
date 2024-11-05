package com.formation_full_stack_java_angular.rest_api.controllers;

import com.formation_full_stack_java_angular.rest_api.entities.User;
import com.formation_full_stack_java_angular.rest_api.entities.Utilisateur;
import com.formation_full_stack_java_angular.rest_api.exceptions.PasUtilisateurException;
import com.formation_full_stack_java_angular.rest_api.modelForm.LoginForm;
import com.formation_full_stack_java_angular.rest_api.repositories.UserRepository;
import com.formation_full_stack_java_angular.rest_api.repositories.UtilisateurRepository;
import com.formation_full_stack_java_angular.rest_api.securityConfig.JwtAuthenticationResponse;
import com.formation_full_stack_java_angular.rest_api.securityConfig.JwtTokenProvider;
import com.formation_full_stack_java_angular.rest_api.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RestAPIController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final List<String> listRoles = List.of("CREATION","CONSULTATION","MODIFICATION","ADMIN","SUPPRESSION");

    // Récupération de la liste complète des utilisateurs
    //@GetMapping("jpa/utilisateurs")
    //public List<Utilisateur> recupererListeUtilisateur() {
    //    return utilisateurRepository.findAll();
    //}

    // Récupération de la liste complète des utilisateurs
    @GetMapping("utilisateurs")
    public ResponseEntity<List<Utilisateur>> recupererListeUtilisateur() {
        return new ResponseEntity<>(utilisateurRepository.findAll(), HttpStatus.OK);
    }

    // Récupération d'un utilisateur par id
//    @GetMapping("jpa/utilisateurs/{id}")
//    public Utilisateur recupererUtilisateurParId(@PathVariable Long id) throws PasUtilisateurException {
//        if(Objects.nonNull(id) && (id>6)) {
//            throw new PasUtilisateurException("Aucun utilisateur trouvé pour l'id défini");
//        }
//        return utilisateurRepository.findById(id).get();
//    }

    // Récupération d'un utilisateur par id

    @GetMapping("utilisateurs/{id}")
    public ResponseEntity<Utilisateur> recupererUtilisateurParId(@PathVariable Long id) throws PasUtilisateurException {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur.isPresent()) {
            return new ResponseEntity<>(utilisateur.get(), HttpStatus.OK);
        } else {
            throw new PasUtilisateurException("Aucun utilisateur trouvé pour l'id défini");

        }
    }

    // Publier / Sauvegarde de données en BDD

    @PostMapping("utilisateurs/creation")
    public ResponseEntity<Utilisateur> creationUtilisateur(@RequestBody Utilisateur nouveauUtilisateur) throws ServerException {
        Utilisateur utilisateur = utilisateurRepository.save(nouveauUtilisateur);

        if (utilisateur == null) {
            throw new ServerException("Erreur de serveur");
        }
        else {

            URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(utilisateur.getId()).toUri();

            return ResponseEntity.created(url).build();
        }
    }

//    @PostMapping("utilisateurs/creation")
//    public ResponseEntity<?> creationUtilisateur(@RequestBody Utilisateur nouveauUtilisateur) {
//        // Check if the user already exists based on the primary key or a unique field (e.g., email)
//        Optional<Utilisateur> existingUtilisateur = utilisateurRepository.findById(nouveauUtilisateur.getId());
//
//        if (existingUtilisateur.isPresent()) {
//            // Return a 409 Conflict response indicating that the user already exists
//            return new ResponseEntity<>("User with the same ID already exists", HttpStatus.CONFLICT);
//        }
//        try {
//            // Save the new user if it doesn't exist
//            Utilisateur utilisateur = utilisateurRepository.save(nouveauUtilisateur);
//            return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle other possible exceptions, such as database errors
//            return new ResponseEntity<>("Error occurred while creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // Suppression de l'utilisateur par id

    @DeleteMapping("utilisateur/suppression/{id}")
    public ResponseEntity<Void> suppressionUtilisateur(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Mise à jour de l'utilisateur

    @PutMapping("utilisateur/miseAjour/{id}")
    public ResponseEntity<Utilisateur> miseAjourUtilisateur(@PathVariable Long id, @RequestBody Utilisateur util) {
        Utilisateur utilisateur = utilisateurRepository.save(util);
    return new ResponseEntity<Utilisateur>(utilisateur, HttpStatus.OK);
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> authUser(@RequestBody LoginForm loginFormRequest) {
        //Rechercher un utilisateur en fonction de son username
        User user = userRepository.findUserByName(loginFormRequest.getUsername());
        if(user!=null && isUser(loginFormRequest, user)) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginFormRequest.getUsername());
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(authority -> listRoles.contains(authority.getAuthority()))) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtTokenProvider.generateToken(authentication);
                System.out.println(token);

                return ResponseEntity.ok(new JwtAuthenticationResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'a pas tous les droits nécessaires");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom utilisateur et mot de passe Incorrect");
        }
    }

    private static boolean isUser(LoginForm loginFormRequest, User user) {
        return user.getUsername().equals(loginFormRequest.getUsername()) && user.getPassword().equals(loginFormRequest.getPassword());
    }
}