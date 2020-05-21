package TradeZone.service;

import TradeZone.data.model.view.ProfileConversationViewModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import TradeZone.data.model.entity.Role;
import TradeZone.data.model.entity.UserProfile;
import TradeZone.data.model.enums.RoleName;
import TradeZone.data.model.entity.User;
import TradeZone.data.model.rest.message.response.JwtResponse;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.UserServiceModel;
import TradeZone.data.repository.RoleRepository;
import TradeZone.data.repository.UserProfileRepository;
import TradeZone.data.repository.UserRepository;
import TradeZone.config.security.jwt.JwtProvider;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final String TAKEN_USERNAME = "Fail -> Username is already taken!";
    private final String TAKEN_EMAIL = "Fail -> Email is already in use!";
    private final String REGISTERED_SUCCESSFULLY = "User registered successfully!";
    private static final String NOT_FOUND_MESSAGE = "User Not Found with -> username or email : ";

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final SimpMessagingTemplate template;

    private final PasswordEncoder encoder;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Override
    public ResponseMessage register(UserServiceModel model) {

        if (userRepository.existsByUsername(model.getUsername())) {
            return new ResponseMessage(TAKEN_USERNAME);
        }

        if (userRepository.existsByEmail(model.getEmail())) {
            return new ResponseMessage(TAKEN_EMAIL);
        }

        if (!roleService.rolesAreSeeded()) {
            roleService.seed();
        }

        User user = mapper.map(model, User.class);
        user.setPassword(encoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElse(null));

        if (fitsModeratorRole() || fitsAdminRole()) {
            roles.add(roleRepository.findByName(RoleName.ROLE_MODERATOR).orElse(null));
        }
        if (fitsAdminRole()) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null));
        }

        user.setRoles(roles);

        user = userRepository.save(user);

        UserProfile userProfile = new UserProfile(user);

        userProfile = userProfileRepository.saveAndFlush(userProfile);

        user.setProfile(userProfile);

        userRepository.save(user);

        return new ResponseMessage(REGISTERED_SUCCESSFULLY);
    }

    @Override
    public JwtResponse login(UserServiceModel user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        connectIfNeeded(userDetails);

        return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }



    private void connectIfNeeded(UserDetails user) {

        UserProfile userProfile = userProfileRepository
                .findByUserUsername(user.getUsername())
                .orElseThrow();

        if (userProfile.getConnected()) {
            return;
        }

        userProfile.setConnected(true);

        userProfileRepository.save(userProfile);

        ProfileConversationViewModel viewModel = mapper.map(userProfile,ProfileConversationViewModel.class);

        template.convertAndSend("/channel/login", viewModel);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_MESSAGE + username));
    }

    private boolean fitsAdminRole() {
        return userRepository.count() == 0;
    }

    private boolean fitsModeratorRole() {
        return userRepository.count() == 1;
    }
}
