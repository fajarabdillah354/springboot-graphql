package codejar.springboot_graphQL.book;


import codejar.springboot_graphQL.author.Author;
import codejar.springboot_graphQL.author.AuthorRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    //@SchemaMapping(typeName = "Query",field = "books")    bisa pake ini dengan menuliskan typename dan field di graphql
    //@QueryMapping(name = "books") bisa pake ini dengan parameter name sebegai source graphqlnya
    @QueryMapping // atau bisa langsung membuat nama method seperti data di Query graphql kita, jika seperti ini tidak perlu memberi parameter apapun
    public List<Book> books(){
        return bookRepository.findAll();
    }

    @QueryMapping
    public Optional<Book> book(@Argument Long id) {
        return bookRepository.findById(id);
    }

    @MutationMapping
    public Book addBook(@Argument BookInput bookInput) {
        var author = authorRepository.findById(bookInput.authorId());
        var book = new Book();
        book.setTitle(bookInput.title());
        book.setAuthor(author.orElseThrow());
        return bookRepository.save(book);
    }








}

