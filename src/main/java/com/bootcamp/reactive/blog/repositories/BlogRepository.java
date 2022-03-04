package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.Blog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface BlogRepository extends ReactiveMongoRepository<Blog, String> {
    Flux<Blog> findByAuthorId(String authorId);
}