package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.BaseException;
import com.example.alleywayalliancelms.exception.CopyNotFoundException;
import com.example.alleywayalliancelms.model.BookCopy;
import com.example.alleywayalliancelms.model.Publisher;
import com.example.alleywayalliancelms.repository.BookCopyRepository;
import com.example.alleywayalliancelms.repository.PublisherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    private final PublisherRepository publisherRepository;

    @Autowired
    public BookCopyService(BookCopyRepository bookCopyRepository,
                           PublisherRepository publisherRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookCopy> getBookCopiesByBookId(Long bookId) {
        return bookCopyRepository.findBookCopiesByBookId(bookId);
    }

    public List<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }

    public void saveWithInternalPublisher(BookCopy bookCopy, Long publisherId) {
        Publisher publisherObj = publisherRepository.findById(publisherId).get();
        bookCopy.setPublisher(publisherObj);
        bookCopy.setPublishId(publisherObj.getId());
        bookCopyRepository.save(bookCopy);
    }

    public void saveWithPublisher(BookCopy bookCopy, Publisher publisher)  {
        publisherRepository.save(publisher);
        bookCopy.setPublisher(publisher);
        bookCopy.setPublishId(publisher.getId());
        bookCopyRepository.save(bookCopy);
    }

    public void deleteBookCopy(Long bookCopyId) {
        try {
            bookCopyRepository.delete(bookCopyRepository.findById(bookCopyId).get());
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument exception at deleteBookCopy: " + e.getMessage());
        }
    }

    public BookCopy getBookCopyById(Long bookCopyId) {
        if (bookCopyRepository.findById(bookCopyId).isPresent()) {
           return bookCopyRepository.findById(bookCopyId).get();
        } else {
            throw new CopyNotFoundException();
        }
    }

}
