package com.bootcamp.reactive.blog.utils;

import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
public class AuthorUtils {


    private final AuthorRepository authorRepository;

    public Mono<Boolean> isMayorEdad(Date birthDate, Date currentDate) {
        return diffDates(birthDate, currentDate) > 18
                ? Mono.just(Boolean.TRUE)
                : Mono.error(new Exception("El author tiene que ser mayor a 18 anios"));
    }

    private int diffDates(Date fechaNac, Date fechaActual) {
        LocalDate birthDate = fechaNac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = fechaActual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, currentDate).getYears();
    }

}
