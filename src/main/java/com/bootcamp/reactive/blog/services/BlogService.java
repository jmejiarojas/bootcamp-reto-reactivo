package com.bootcamp.reactive.blog.services;

import com.bootcamp.reactive.blog.dto.BlogRequest;
import com.bootcamp.reactive.blog.entities.Blog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlogService {
    Mono<Blog> findById(String id);
    Flux<Blog> findAll();
    Mono<Blog> save(Blog blog);
    Mono<Void> delete(String id);

    Mono<Long> countBlogsByAuthor(String idAuthor);
    Mono<Blog> createBlog(BlogRequest blogRequest);
}
