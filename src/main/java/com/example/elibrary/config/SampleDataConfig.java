package com.example.elibrary.config;

import com.example.elibrary.domain.Book;
import com.example.elibrary.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDataConfig {

    @Bean
    CommandLineRunner loadSampleBooks(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                bookRepository.save(new Book(
                        "Clean Code", "Robert C. Martin", "9780132350884", 3));
                bookRepository.save(new Book(
                        "Effective Java", "Joshua Bloch", "9780134685991", 2));
                bookRepository.save(new Book(
                        "The Pragmatic Programmer", "Andrew Hunt", "9780201616224", 4));
            }
        };
    }
}
