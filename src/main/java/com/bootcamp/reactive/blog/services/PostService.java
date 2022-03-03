package com.bootcamp.reactive.blog.services;

import com.bootcamp.reactive.blog.entities.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
    Mono<Post> save(Post post);
    Flux<Post> findAll();
}
