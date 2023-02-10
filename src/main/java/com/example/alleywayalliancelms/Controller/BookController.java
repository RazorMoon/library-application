package com.example.alleywayalliancelms.Controller;

import com.example.alleywayalliancelms.Exception.BookNotFoundException;
import com.example.alleywayalliancelms.Model.Author;
import com.example.alleywayalliancelms.Model.Book;
import com.example.alleywayalliancelms.Model.Category;
import com.example.alleywayalliancelms.Model.Genre;
import com.example.alleywayalliancelms.Service.BookService;
import com.example.alleywayalliancelms.Service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/catalog")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private NotificationService notificationService;


    @GetMapping
    public String showCatalogPage(Model model, Authentication authentication) {
        model.addAttribute("bookList", bookService.getListOfAllBooks());
        model.addAttribute("bookCount", bookService.getBookCount());
        model.addAttribute("categoriesList", bookService.getCategories());
        model.addAttribute("genresList", bookService.getGenres());
        model.addAttribute("notificationList", notificationService.getNotificationsList(
                notificationService.convertPatronEmailToId(authentication.getName())));
        return "/catalog/catalog";
    }

    @ModelAttribute
    public List<Book> books() {
        return new ArrayList<>();
    }

    @GetMapping("/addBook")
    public String showAddBookPage(Model model, @ModelAttribute("errors") String formErrors) {
        model.addAttribute("bookForm", new Book());
        model.addAttribute("authorForm", new Author());
        model.addAttribute("authorList", bookService.getAuthors());
        model.addAttribute("categoryList", bookService.getCategories());
        model.addAttribute("genreList", bookService.getGenres());
        model.addAttribute("formErrors", formErrors);
        return "/copies/addBook";
    }

    @PostMapping("/addBook")
    public String postAddBookForm(@Valid @ModelAttribute("bookForm") Book bookForm,
                                  Errors errors,
                                  Model model,
                                  @ModelAttribute("authorForm") Author authorForm,
                                  @RequestParam(required = true, defaultValue = "") Long authorId,
                                  @RequestParam(required = true, defaultValue = "false") Boolean action,
                                  RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", "В форме имеются ошибки!");
            return "redirect:/catalog/addBook";
        }

        if (action) {
            bookService.saveWithInternalAuthor(bookForm, authorId);
        } else {
            bookService.saveWithAuthor(bookForm, authorForm);
        }
        return "redirect:/catalog";
    }

    @GetMapping("/deleteBook")
    public String showDeleteBookPage() {
        return "catalog/deleteBook";
    }

    @PostMapping("/deleteBook")
    public String postDeleteBook(@RequestParam(required = true, defaultValue = "") Long bookId,
                                 RedirectAttributes redirectAttributes) throws BookNotFoundException {

        Book redirectBook = bookService.getBookById(bookId);
        redirectAttributes.addFlashAttribute("redirectBook", redirectBook);

        return "redirect:/catalog/deleteBook/confirm";
    }

    @GetMapping("/deleteBook/confirm")
    public String showDeleteBookConfirm(Model model,
                                        @ModelAttribute("redirectBook") Book redirectBook) {
        model.addAttribute("redirectBook", redirectBook);
        return "/catalog/deleteconfirm";
    }

    @PostMapping("/deleteBook/confirm")
    public String postDeleteBookConfirm(@RequestParam(required = true, defaultValue = "") Long bookId,
                                        @RequestParam(required = true, defaultValue = "") String action) {

        if (action.equals("delete")) {
            bookService.deleteBook(bookId);
            return "redirect:/catalog";
        } else {
            return "redirect:/catalog";
        }

    }

    @GetMapping("/addcategory")
    public String showAddCategoryPage(Model model) {
        model.addAttribute("categoryForm", new Category());
        return "/catalog/addBookCategory";
    }

    @PostMapping("/addcategory")
    public String postAddCategoryPage(@Valid @ModelAttribute("categoryForm") Category categoryForm,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            return "/catalog/addBookCategory";
        }

        bookService.saveCategory(categoryForm);
        return "redirect:/catalog";
    }

    @GetMapping("/addgenre")
    public String getAddGenrePage(Model model) {
        model.addAttribute("genreForm", new Genre());
        return "/catalog/addBookGenre";
    }

    @PostMapping("/addgenre")
    public String postAddGenrePage(@Valid @ModelAttribute("genreForm") Genre genreForm,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "/catalog/addBookGenre";
        }

        bookService.addGenre(genreForm);
        return "redirect:/catalog";
    }

    @GetMapping("/addauthor")
    public String getAddAuthorPage(Model model, @ModelAttribute("formErrors") String errors) {
        model.addAttribute("authorForm", new Author());
        model.addAttribute("authorList", bookService.getAuthors());
        model.addAttribute("formErrors", errors);
        model.addAttribute("bookList", bookService.getListOfAllBooks());
        return "/catalog/addAuthorPage";
    }

    @PostMapping("/addauthor")
    public String postAddAuthorPage(@Valid @ModelAttribute("authorForm") Author author,
                                    BindingResult bindingResult,
                                    Model model,
                                    @RequestParam(required = true, defaultValue = "false") Boolean action,
                                    @RequestParam(required = true, defaultValue = "") Long authorId,
                                    @RequestParam(required = true, defaultValue = "") Long book,
                                    RedirectAttributes redirectAttributes) {


        if (action) {
            bookService.saveExistingAuthorsToExistingBook(book, authorId);
        } else {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("formErrors", "В форме имеются ошибки!");
                return "redirect:/catalog/addauthor";
            }
            bookService.saveNewAuthorToExistingBook(book, author);
        }

        return "redirect:/catalog";
    }

}
