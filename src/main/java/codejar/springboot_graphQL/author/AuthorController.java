package codejar.springboot_graphQL.author;


import codejar.springboot_graphQL.DataLouder;
import codejar.springboot_graphQL.book.Book;
import codejar.springboot_graphQL.book.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public List<Author> authors() {
        //return authorRepository.findAllWithBooks();
        return authorRepository.findAll();
    }

    @SchemaMapping
    public List<Book> booksWithDelay(Author author) throws InterruptedException {
        // this could be a call to some microservice would retrieve books by authorId
        log.info("Retrieving books for author " + author.getName());
        Thread.sleep((1000));
        return new ArrayList<>();
    }

//    @SchemaMapping
//    public List<Book> books(Author author) throws InterruptedException {
//        log.info("Retrieving books for author " + author.getName());
//        Thread.sleep((1000));
//        return bookRepository.findByAuthor(author);
//    }

    @BatchMapping
    public List<List<Book>> books(List<Author> authors) {
        log.info("Batch loading books for {} authors", authors.size());

        // Get all author IDs
        List<Long> authorIds = authors.stream()
                .map(Author::getId)
                .toList();

        // Make a single query to get all books for all authors
        List<Book> allBooks = bookRepository.findByAuthorIdIn(authorIds);

        // Group books by author ID
        Map<Long, List<Book>> booksByAuthorId = allBooks.stream()
                .collect(Collectors.groupingBy(book -> book.getAuthor().getId()));

        // Map back to original author order
        return authors.stream()
                .map(author -> booksByAuthorId.getOrDefault(author.getId(), Collections.emptyList()))
                .toList();
    }



    // imagine the application is microservices where have the threads

    /*
    we can solve by add some yaml configuration for virtual threads and make it enabled is true
     */
//    @SchemaMapping
//    public List<Book> books(Author author) throws  InterruptedException{
//        log.info("Getting books for {}", author);
//        Thread.sleep(1000);
//        return new ArrayList<>();
//    }


    /*
    @BatchMapping adalah annotation di Spring for GraphQL yang dipakai untuk resolve field relasi secara batch (sekaligus untuk banyak parent object), supaya otomatis menghindari N+1 problem — tanpa kamu harus setup DataLoader secara manual.
     */



}
