package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.AuthorExistsException;
import com.bootcamp.reactive.blog.core.exception.UserExistsException;
import com.bootcamp.reactive.blog.entities.User;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.UserRepository;
import com.bootcamp.reactive.blog.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
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
}
