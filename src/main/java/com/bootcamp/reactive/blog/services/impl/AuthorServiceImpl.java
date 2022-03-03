package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.AuthorExistsException;
import com.bootcamp.reactive.blog.core.exception.AuthorNotFoundException;
import com.bootcamp.reactive.blog.entities.Author;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

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
                .flatMap(exists->
                {
                    return !exists ? this.authorRepository.save(author): Mono.error(new AuthorExistsException("Author exists"));
                });

    }

    @Override
    public Mono<Void> delete(String id) {

        return this.authorRepository.findById(id)
            .switchIfEmpty(Mono.error(new AuthorNotFoundException("Author no encontrado")))
            .flatMap(author-> {
                return this.authorRepository.delete(author);
            });



//        return this.authorRepository.findById(id)
//                .flatMap(author-> this.authorRepository.delete(author));


//        return this.authorRepository.deleteById(id);

    }

}
