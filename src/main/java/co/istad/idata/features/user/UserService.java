package co.istad.idata.features.user;

import co.istad.idata.features.user.dto.UserCreateRequest;
import co.istad.idata.features.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    void createUser(UserCreateRequest request);
    List<UserResponse> findAll();

}
