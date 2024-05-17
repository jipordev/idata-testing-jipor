package co.istad.idata.features.user.dto;

public record UserCreateRequest(
        String username,
        String password,
        String confirmedPassword
) {
}
