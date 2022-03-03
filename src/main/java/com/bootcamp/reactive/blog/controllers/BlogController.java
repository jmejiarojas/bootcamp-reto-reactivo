package com.bootcamp.reactive.blog.controllers;

import com.bootcamp.reactive.blog.entities.Blog;
import com.bootcamp.reactive.blog.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
//@RequestMapping("blogs")
public class BlogController {

    @Autowired
    private  BlogService blogService;

//    @GetMapping("{id}")
//    public Mono<Blog> findById(@PathVariable String id){
//        return blogService.findById(id);
//    }
//
//    @GetMapping()
//    public Flux<Blog> findAll(){
//        return blogService.findAll();
//    }

//    @PostMapping()
//    public Mono<Blog> save(@RequestBody Blog blog){
//
//        return blogService.save(blog);
//    }

}
