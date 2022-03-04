package com.bootcamp.reactive.blog.services.impl;


import com.bootcamp.reactive.blog.core.exception.AuthorNotFoundException;
import com.bootcamp.reactive.blog.entities.Blog;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.services.BlogService;
import com.bootcamp.reactive.blog.utils.AuthorUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;


@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final AuthorRepository authorRepository;
    private final AuthorUtils authorUtils;

    @Override
    public Mono<Blog> findById(String id) {
        return this.blogRepository.findById(id);
    }

    @Override
    public Flux<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Mono<Blog> save(Blog blog) {

        return this.authorRepository.findById(blog.getAuthorId())
                .flatMap(author -> this.authorUtils.isMayorEdad(author.getBirthDate(), new Date())
                        .map(aBoolean -> Pair.of(author, aBoolean))
                )
                .flatMap(authorBooleanPair -> this.blogRepository.findByAuthorId(authorBooleanPair.getFirst().getId())
                        .count()
                        .flatMap(aLong -> (aLong >= 3)
                                ? Mono.error(new Exception("Un autor puede tener maximo 3 blogs"))
                                : blogRepository.save(blog)))
                .switchIfEmpty(Mono.error(new AuthorNotFoundException("No existe author con dicho id")));
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.blogRepository.findById(id)
                .doOnNext(b -> {
                    System.out.println("doOnNext b = " + b);
                })
                .flatMap(this.blogRepository::delete);

    }
}