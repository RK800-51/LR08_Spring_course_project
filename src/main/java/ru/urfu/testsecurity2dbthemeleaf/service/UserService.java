package ru.urfu.testsecurity2dbthemeleaf.service;

import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
