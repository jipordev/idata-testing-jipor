package co.istad.idata.mapper;

import co.istad.idata.domains.auth.User;
import co.istad.idata.features.user.dto.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    List<UserResponse> toUserResponse(List<User> users);

}
