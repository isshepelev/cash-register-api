package ru.isshepelev.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoApplication.class, args);
    }

}


// TODO добавить удаление редактирование и тд во внешнее api (работа с menu и работа с order) если конечно не будет лень
// TODO сделать документацию в redoc
