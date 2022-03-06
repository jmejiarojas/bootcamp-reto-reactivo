package com.bootcamp.reactive.blog.services.impl;


import com.bootcamp.reactive.blog.core.exception.AuthorNotFoundException;
import com.bootcamp.reactive.blog.dto.BlogRequest;
import com.bootcamp.reactive.blog.entities.Blog;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.services.BlogService;
import com.bootcamp.reactive.blog.utils.AuthorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;


@Service
@RequiredArgsConstructor
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
            .flatMap(author -> this.authorUtils.isMayorEdad(author.getBirthDate(), LocalDate.now())
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

  @Override
  public Mono<Long> countBlogsByAuthor(String idAuthor) {
    return blogRepository.findByAuthorId(idAuthor).count();
  }

  @Override
  public Mono<Blog> createBlog(BlogRequest blogRequest) {
    return blogRepository.findByAuthorId(blogRequest.getAuthorId()).count()
            .filter(aLong -> aLong < 3)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("limite de blogs."))))
            .flatMap(aLong -> authorRepository.findById(blogRequest.getAuthorId()))
            .filter(author -> Period.between(author.getBirthDate(), LocalDate.now()).getYears() > 18)
            .map(aLong -> Blog.builder()
                    .authorId(blogRequest.getAuthorId())
                    .name(blogRequest.getName())
                    .url(blogRequest.getUrl())
                    .status("activo").build())
            .flatMap(blogRepository::save)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("es menor de 18 anhos."))));
  }
}