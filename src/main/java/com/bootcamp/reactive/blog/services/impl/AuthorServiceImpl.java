package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.AuthorExistsException;
import com.bootcamp.reactive.blog.core.exception.AuthorNotFoundException;
import com.bootcamp.reactive.blog.entities.Author;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.repositories.PostRepository;
import com.bootcamp.reactive.blog.services.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;

    @Override
    public Mono<Author> findById(String id) {
        return this.authorRepository.findById(id);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return authorRepository.existsByEmail(email);
    }

    @Override
    public Flux<Author> findByEmail(String email) {
//        var authorFilter = new Author();
//        authorFilter.setEmail(email);
//
//        return this.authorRepository.findAll(Example.of(authorFilter));

        return this.authorRepository.findByEmail(email);
    }

    @Override
    public Flux<Author> findByName(String name) {
        return this.authorRepository.findByName(name);
    }

    @Override
    public Flux<Author> findAll() {
        return this.authorRepository.findAll();
    }

    @Override
    public Mono<Author> save(Author author) {
        return this.authorRepository.save(author);
    }

    @Override
    public Mono<Author> saveWithValidation(Author author) {

//        return this.authorRepository.existsByEmail(author.getEmail())
//                .flatMap(exists->
//                        {
//                            return exists ? Mono.empty():this.authorRepository.save(author);
//                        });

        return this.authorRepository.existsByEmail(author.getEmail())
                .flatMap(exists ->
                {
                    return !exists ? this.authorRepository.save(author) : Mono.error(new AuthorExistsException("Author exists"));
                });

    }

    @Override
    @Transactional
    public Mono<Void> delete(String id) {

        return this.authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new AuthorNotFoundException("Author no encontrado")))
                .flatMap(author -> {
                    this.blogRepository.findByAuthorId(author.getId()).flatMap(blog -> this.blogRepository.deleteById(blog.getId()));
                    this.blogRepository.findByAuthorId(author.getId()).flatMap(blog -> this.postRepository.findByBlogId(blog.getId()).flatMap(post -> this.postRepository.deleteById(post.getId())));
                    return this.authorRepository.deleteById(author.getId());
                });

//        return this.authorRepository.findById(id)
//                .switchIfEmpty(Mono.error(new AuthorNotFoundException("Author no encontrado")))
//                .flatMap(author -> this.blogRepository.findByAuthorId(author.getId())
//                        .map(blog -> Pair.of(blog, author))
//                        .flatMap(blogAuthorPair -> this.postRepository
//                                .findByBlogId(blogAuthorPair.getFirst().getId())
//                                .map(post -> Pair.of(blogAuthorPair, post))
//                                .flatMap(pairPostPair -> {
//                                    this.postRepository.delete(pairPostPair.getSecond());
//                                    this.blogRepository.delete(pairPostPair.getFirst().getFirst());
//                                    this.authorRepository.deleteById(pairPostPair.getFirst().getSecond().getId());
//                                })
//                        )
//                );


//        return this.authorRepository.findById(id)
//                .flatMap(author-> this.authorRepository.delete(author));


//        return this.authorRepository.deleteById(id);

    }

}
