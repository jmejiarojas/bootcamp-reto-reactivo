package com.bootcamp.reactive.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterPostRequest {

  private String title;
  private LocalDate date;
  private String status;
  private String content;
  private String blogId;
}
