package com.bootcamp.reactive.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterRequest {

  private String Name;
  private String email;
  private String phoneNumber;
  private Date birthDate;
  private String userName;
  private String password;
}
