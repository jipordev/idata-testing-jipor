package co.istad.idata.features.user.dto;

public record UserResponse(
        String username,
        String password,
        String uuid
) {
}
