package co.com.techskill.lab2.library.service.impl;

import co.com.techskill.lab2.library.config.BookMapper;
import co.com.techskill.lab2.library.config.BookMapperImpl;
import co.com.techskill.lab2.library.domain.dto.BookDTO;
import co.com.techskill.lab2.library.repository.IBookRepository;
import co.com.techskill.lab2.library.service.IBookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BookServiceImpl implements IBookService {
    private final IBookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(IBookRepository bookRepository){
        this.bookRepository = bookRepository;
        this.bookMapper = new BookMapperImpl();
    }


    @Override
    public Flux<BookDTO> findAll() {
        return bookRepository
                .findAll()
                .map(bookMapper::toDTO);
    }

    @Override
    public Mono<BookDTO> findById(String id) {
        return bookRepository
                .findByBookId(id) //MongoReactive: 1 - Mono<Book> | 0 - Mono.empty(); Se ejecuta el switchIfEmpty!
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found or available")))
                .flatMap(book -> {
                    if(!book.getAvailable()){
                        return Mono.error(new RuntimeException("Book not available"));
                    }
                    return Mono.just(book);
                })
                .map(book -> bookMapper.toDTO(book));

    }

    @Override
    public Mono<BookDTO> save(BookDTO bookDTO) {
        bookDTO.setBookId(UUID.randomUUID().toString().substring(0,10));
        bookDTO.setAvailable(true);
        return bookRepository
                .save(bookMapper.toEntity(bookDTO))
                .map(bookMapper::toDTO);

    }



}
