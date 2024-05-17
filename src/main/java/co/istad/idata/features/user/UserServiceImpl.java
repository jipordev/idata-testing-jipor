package co.istad.idata.features.user;

import co.istad.idata.domains.auth.User;
import co.istad.idata.features.user.dto.UserCreateRequest;
import co.istad.idata.features.user.dto.UserResponse;
import co.istad.idata.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void createUser(UserCreateRequest request) {

        User existedUser = userRepository.findByUsername(request.username());

        if (existedUser !=null && request.username().equals(existedUser.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already existed"
            );
        }

        if (!request.password().equals(request.confirmedPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password is not match"
            );
        }

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(request.password());
        newUser.setUuid(UUID.randomUUID().toString());

        userRepository.save(newUser);

    }

    @Override
    public List<UserResponse> findAll() {

        List<User> userList = userRepository.findAll();

        return userMapper.toUserResponse(userList);

    }
}
