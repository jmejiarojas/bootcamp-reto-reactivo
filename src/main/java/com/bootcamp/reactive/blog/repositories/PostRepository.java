package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Flux<Post> findByStatusEquals(String status);
    Flux<Post> findByBlogId(String blogId);
}
