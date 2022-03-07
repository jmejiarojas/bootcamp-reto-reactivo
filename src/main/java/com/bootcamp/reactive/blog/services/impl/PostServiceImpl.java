package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.BlogNotFoundException;
import com.bootcamp.reactive.blog.dto.PostRequest;
import com.bootcamp.reactive.blog.dto.RegisterPostRequest;
import com.bootcamp.reactive.blog.entities.Post;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.repositories.PostRepository;
import com.bootcamp.reactive.blog.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final BlogRepository blogRepository;

  // Los posts se registran siempre en estado borrador, asi que por dia se puede tener mas de un post siempre y cuando esten en borradores.
  @Override
  public Mono<Post> save(PostRequest postRequest) {
    // Buscamos el blog y validamos si esta en estado activo

    return this.blogRepository.findById(postRequest.getBlogId())
            .flatMap(blog -> {
              if (blog.getStatus().equals("inactivo")) {
                return Mono.error(new Exception("Solo se registran post en blogs con estado activo"));
              } else {
                // Aca meteremos la logica para si es que tiene comentarios validar que el post tenga estado publicado
                if(!postRequest.getComments().isEmpty() && postRequest.getStatus().equals("borrador")) {
                  return Mono.error(new Exception("Solo se pueden registrar comentarios con estado publicado"));
                }
                return this.postRepository.save(this.postDtoToPost(postRequest));
              }
            })
            .switchIfEmpty(Mono.error(new BlogNotFoundException()));
  }

  @Override
  public Flux<Post> findAll() {
    return this.postRepository.findAll();
  }

  @Override
  public Mono<String> registerDraftPost(RegisterPostRequest registerPostRequest) {
    return blogRepository.findById(registerPostRequest.getBlogId())
            .filter(blog -> blog.getStatus().equals("activo"))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Blog inactivo!!"))))
            .flatMap(blog -> postRepository.findByBlogId(registerPostRequest.getBlogId())
                    .filter(post -> post.getDate().getDayOfMonth() == LocalDate.now().getDayOfMonth())
                    .count())
            .filter(aLong -> aLong < 1)
            .map(aLong -> Post.builder()
                    .blogId(registerPostRequest.getBlogId())
                    .content(registerPostRequest.getContent())
                    .date(registerPostRequest.getDate())
                    .title(registerPostRequest.getTitle())
                    .status("borrador").build())
            .flatMap(postRepository::save)
            .map(post -> "Post Register Ok!")
            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Solo 1 post por dia!!"))));
  }

  // Para publicar un post o poner estado "publicado" se actualiza el estado del post, es la unica forma, es mi regla de negocio
  @Override
  public Mono<Post> publish(String id) {
    return this.postRepository.findById(id)
            .flatMap(post -> this.postRepository.findByStatusEquals("publicado").any(post1 -> sameDay(post1.getDate(), LocalDate.now()))
                    .map(aBoolean -> Pair.of(aBoolean, post))
                    .flatMap(booleanPostPair -> {
                      if (booleanPostPair.getFirst()) {
                        return Mono.error(new Exception("No se puede publicar mas de un post por dia"));
                      } else {
                        Post myPost = booleanPostPair.getSecond();
                        myPost.setStatus("publicado");
                        return this.postRepository.save(myPost);
                      }

                    }))
            .switchIfEmpty(Mono.error(new BlogNotFoundException()));

  }

  private boolean sameDay(LocalDate fechaPost, LocalDate fechaActual) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    return fmt.format(fechaPost).equals(fmt.format(fechaActual));
  }

  // La fecha no deberia ser ingresada por el usuario, ya que la fecha es la del dia
  private Post postDtoToPost(PostRequest postDto) {
    Post post = new Post();
    post.setTitle(postDto.getTitle());
    post.setStatus(postDto.getStatus());
    post.setContent(postDto.getContent());
    post.setDate(LocalDate.now());
    post.setBlogId(postDto.getBlogId());
    return post;
  }



}
