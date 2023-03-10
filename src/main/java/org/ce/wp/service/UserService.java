package org.ce.wp.service;

import org.ce.wp.dto.SignUpRequestDto;
import org.ce.wp.dto.SignUpResponseDto;
import org.ce.wp.entity.User;

import java.util.Optional;

/**
 * @author Aroutin Nazarian
 * @since 20.01.23
 */
public interface UserService {

    Optional<User> getUser(String username);

    boolean signUpUser(SignUpRequestDto requestDto);
}
