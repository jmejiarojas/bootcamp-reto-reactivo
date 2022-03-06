package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.AuthorExistsException;
import com.bootcamp.reactive.blog.core.exception.UserExistsException;
import com.bootcamp.reactive.blog.dto.RegisterRequest;
import com.bootcamp.reactive.blog.entities.Author;
import com.bootcamp.reactive.blog.entities.User;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.UserRepository;
import com.bootcamp.reactive.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final AuthorRepository authorRepository;

  @Override
  public Mono<Boolean> login(String login, String password) {
    return this.userRepository.findByLoginAndPassword(login, password).hasElements();
  }

  @Override
  public Mono<User> save(User user) {

    return this.authorRepository.findById(user.getAuthorId())
            .flatMap(author -> this.userRepository.findByAuthorId(author.getId())
                    .hasElements()
                    .flatMap(aBoolean -> !aBoolean
                            ? this.userRepository.save(user)
                            : Mono.error(new UserExistsException("Ya existe un usuario para dicho author")))
            )
            .switchIfEmpty(
                    Mono.error(new AuthorExistsException("No existe author con dicho id"))
            );

  }

  @Override
  public Mono<String> register(RegisterRequest registerRequest) {
    return authorRepository.existsByEmail(registerRequest.getEmail())
            .filter(not(existEmail))
            .map(aBoolean -> Author.builder()
                    .email(registerRequest.getEmail())
                    .birthDate(registerRequest.getBirthDate())
                    .name(registerRequest.getName())
                    .phone(registerRequest.getPhoneNumber()).build())
            .flatMap(authorRepository::save)
            .map(author -> User.builder()
                    .authorId(author.getId())
                    .login(registerRequest.getUserName())
                    .password(registerRequest.getPassword()).build())
            .flatMap(userRepository::save)
            .map(user -> "new user done!")
            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("User duplicated!"))));
  }

  private Predicate<Boolean> existEmail = aBoolean -> aBoolean;

}
