package com.bootcamp.reactive.blog.services;

import com.bootcamp.reactive.blog.dto.PostRequest;
import com.bootcamp.reactive.blog.dto.RegisterPostRequest;
import com.bootcamp.reactive.blog.entities.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
    Mono<Post> save(PostRequest post);
    Mono<Post> publish(String id);
    Flux<Post> findAll();

    Mono<String> registerDraftPost(RegisterPostRequest registerPostRequest);
}
