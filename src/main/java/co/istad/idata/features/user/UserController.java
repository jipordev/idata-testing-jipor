package co.istad.idata.features.user;

import co.istad.idata.features.user.dto.UserCreateRequest;
import co.istad.idata.features.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    List<UserResponse> findAll(){
        return userService.findAll();
    }

    @PostMapping
    void createUser(@RequestBody @Valid UserCreateRequest request){
        userService.createUser(request);
    }

}
