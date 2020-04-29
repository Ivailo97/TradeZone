package TradeZone.web.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.rest.LoginForm;
import TradeZone.data.model.rest.SignUpForm;
import TradeZone.data.model.rest.message.response.JwtResponse;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.UserServiceModel;
import TradeZone.service.AuthenticationService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    private final ModelMapper mapper;

    private final AuthenticationService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm model) {

        UserServiceModel userServiceModel = mapper.map(model,UserServiceModel.class);

        JwtResponse jwtResponse =  authService.login(userServiceModel);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm model) {

        UserServiceModel userServiceModel = mapper.map(model, UserServiceModel.class);

        ResponseMessage responseMessage = authService.register(userServiceModel);

        HttpStatus status = responseMessage.getMessage().contains("Fail") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;

        return new ResponseEntity<>(responseMessage, status);
    }
}
