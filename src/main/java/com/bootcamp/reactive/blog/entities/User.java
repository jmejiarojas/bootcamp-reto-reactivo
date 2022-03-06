package com.bootcamp.reactive.blog.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(value="users")
public class User {
    private String id;
    private String login;
    private String password;
    private String authorId;
}
