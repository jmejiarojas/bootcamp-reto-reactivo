package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
