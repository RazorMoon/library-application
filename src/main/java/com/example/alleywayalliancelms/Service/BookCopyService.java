package com.example.alleywayalliancelms.Service;

import com.example.alleywayalliancelms.Model.BookCopy;
import com.example.alleywayalliancelms.Model.Publisher;
import com.example.alleywayalliancelms.Repository.BookCopyRepository;
import com.example.alleywayalliancelms.Repository.PublisherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class BookCopyService {

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public List<BookCopy> getBookCopyList() {
        return bookCopyRepository.findAll();
    }

    public List<BookCopy> getBookCopiesByBookId(Long bookId) {
        return bookCopyRepository.findBookCopiesByBookId(bookId);
    }

    public List<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }


    public void saveWithInternalPublisher(BookCopy bookCopy, Long publisherId) throws IllegalArgumentException {
        Publisher publisherObj = publisherRepository.findById(publisherId).get();
        bookCopy.setPublisher(publisherObj);
        bookCopy.setPublishId(publisherObj.getId());
        bookCopyRepository.save(bookCopy);
    }


    public void saveWithPublisher(BookCopy bookCopy, Publisher publisher) throws IllegalArgumentException {
        publisherRepository.save(publisher);
        bookCopy.setPublisher(publisher);
        bookCopy.setPublishId(publisher.getId());
        bookCopyRepository.save(bookCopy);
    }

    public void deleteBookCopy(Long bookCopyId) {
        bookCopyRepository.delete(bookCopyRepository.findById(bookCopyId).get());
    }

    public BookCopy getBookCopyById(Long bookCopyId) {
        return bookCopyRepository.findById(bookCopyId).get();
    }

    public Long getTotalCopies() {
        return bookCopyRepository.count();
    }

    public Long getCopiesOfTheBookId(Long id) {
        return bookCopyRepository.getBookCopiesCount(id);
    }

    public BookCopy save(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }
}
