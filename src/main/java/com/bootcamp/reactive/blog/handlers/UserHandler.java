package com.bootcamp.reactive.blog.handlers;

import com.bootcamp.reactive.blog.entities.Author;
import com.bootcamp.reactive.blog.entities.User;
import com.bootcamp.reactive.blog.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@AllArgsConstructor
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> login(ServerRequest request){
        var login= request.queryParam("login").get();
        var password=request.queryParam("password").get();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(this.userService.login(login, password), Boolean.class);

    }

    public Mono<ServerResponse> save(ServerRequest request) {
        var userInput= request.bodyToMono(User.class);

        return userInput
                .flatMap(this.userService::save)
                .flatMap(a-> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(a), User.class));
    }
}
