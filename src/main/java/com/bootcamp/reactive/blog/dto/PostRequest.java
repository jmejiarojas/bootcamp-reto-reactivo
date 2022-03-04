package com.bootcamp.reactive.blog.dto;

import com.bootcamp.reactive.blog.entities.Comment;
import com.bootcamp.reactive.blog.entities.Reaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;
    private String blogId;
    private String status;
    private List<Comment> comments;
    private List<Reaction> reactions;
}
