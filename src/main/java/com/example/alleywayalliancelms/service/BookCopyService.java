package com.example.alleywayalliancelms.service;

import com.example.alleywayalliancelms.exception.CopyNotFoundException;
import com.example.alleywayalliancelms.model.BookCopy;
import com.example.alleywayalliancelms.model.Publisher;
import com.example.alleywayalliancelms.repository.BookCopyRepository;
import com.example.alleywayalliancelms.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    private final PublisherRepository publisherRepository;

    public BookCopy checkBookCopyIdForNull(Optional<BookCopy> bookCopyOptional) {
        if (bookCopyOptional.isPresent()) {
            return bookCopyOptional.get();
        } else {
            log.error("Book Copy is not found.");
            throw new CopyNotFoundException();
        }
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

    public void saveWithPublisher(BookCopy bookCopy, Publisher publisher) {
        publisherRepository.save(publisher);
        bookCopy.setPublisher(publisher);
        bookCopy.setPublishId(publisher.getId());
        bookCopyRepository.save(bookCopy);
    }

    public void deleteBookCopy(Long bookCopyId) {
        bookCopyRepository.delete(checkBookCopyIdForNull(bookCopyRepository.findById(bookCopyId)));
    }

    public BookCopy getBookCopyById(Long bookCopyId) {
        return checkBookCopyIdForNull(bookCopyRepository.findById(bookCopyId));
    }

}
