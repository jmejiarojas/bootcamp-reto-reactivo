package com.bootcamp.reactive.blog.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(value="posts")
public class Post {
    private String id;
    private String title;
    private LocalDate date;
    private String status;
    private String content;
    private String blogId;
    private List<Comment> comments;
    private List<Reaction> reactions;
}
