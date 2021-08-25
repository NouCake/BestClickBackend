package org.unitedinternet.kevfischer.BestClick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.unitedinternet.kevfischer.BestClick.controller.service.RandomGeneratorService;
import org.unitedinternet.kevfischer.BestClick.model.RegisterRequest;
import org.unitedinternet.kevfischer.BestClick.model.database.User;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAppData;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;
import org.unitedinternet.kevfischer.BestClick.model.database.UserProfile;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserProfileRepository;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private UserProfileRepository profileRepository;
    @Autowired private UserAppRepository appRepository;
    @Autowired private RandomGeneratorService service;

    @GetMapping("/hello")
    public Iterable<User> hello(){
        return userRepository.findAll();
    }

    @GetMapping("/leaderboard")
    public List<UserAppData> getLeaderboard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size){
        var data = appRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "counter")));
        return data;
    }

    @GetMapping("/{uuid}")
    public User getUser(@PathVariable UUID uuid) {
        return ControllerUtil.getOptionalOrThrowStatus(userRepository.findById(uuid));
    }

    @GetMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(@RequestBody List<UUID> uuids) {
        List<User> users = new ArrayList<>(uuids.size());
        userRepository.findAllById(uuids).forEach(users::add);
        return users;
    }

    @GetMapping("/profile/{uuid}")
    public UserProfile getProfile(@PathVariable UUID uuid){
        return ControllerUtil.getOptionalOrThrowStatus(profileRepository.findById(uuid));
    }

    @GetMapping("/profiles")
    public List<UserProfile> getProfiles(@RequestBody List<UUID> uuids){
        List<UserProfile> profiles = new ArrayList<>(uuids.size());
        profileRepository.findAllById(uuids).forEach(profiles::add);
        return profiles;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setId(UUID.randomUUID());

        UserProfile profile = new UserProfile(user, req.getName(), req.getProfile(), req.getEmail());
        UserAppData appData = new UserAppData(user, 0L);
        UserAuthData authData = new UserAuthData(user, req.getUsername().toLowerCase(),req.getPasswordHash());

        userRepository.save(user);
    }

    @PostMapping("/create/random")
    public void createRandomUser(@RequestParam int amount) {
        if(amount > 5000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String[] names = service.getRandomUserName(amount).getBody();
        if(names == null) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        for (String name : names) {
            String username = name.toLowerCase().replaceAll(" ", "");
            String email = username + "@email.com";
            createUser(new RegisterRequest(username, "12345", name, email, null));
        }
    }

}