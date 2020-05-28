package com.example.demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.controller.BookController;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Slf4j
public class SpringbootMysqlJpaApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(SpringbootMysqlJpaApplication.class);
	@Autowired
    private BookRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootMysqlJpaApplication.class, args);
	}
	@Override
    public void run(String... args) {

        log.info("StartApplication...");

        repository.save(new Book("Java"));
        repository.save(new Book("Node"));
        repository.save(new Book("Python"));

        System.out.println("\nfindAll()");
        repository.findAll().forEach(x -> log.info(x.toString()));

        System.out.println("\nfindById(1L)");
        repository.findById(1l).ifPresent(x -> log.info(x.toString()));

        System.out.println("\nfindByName('Node')");
        repository.findByName("Node").forEach(x -> log.info(x.toString()));

    }
}
