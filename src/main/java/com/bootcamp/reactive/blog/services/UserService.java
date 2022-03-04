package com.bootcamp.reactive.blog.services;

import com.bootcamp.reactive.blog.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Boolean> login(String login, String password);
    Mono<User> save(User user);
}
