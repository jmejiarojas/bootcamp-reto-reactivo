package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Flux<User> findByLoginAndPassword(String login, String password);
    Flux<User> findByAuthorId(String authorId);
}
