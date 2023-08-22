package com.example.alleywayalliancelms.controller;

import com.example.alleywayalliancelms.exception.BookNotFoundException;
import com.example.alleywayalliancelms.model.BookCopy;
import com.example.alleywayalliancelms.model.Hold;
import com.example.alleywayalliancelms.model.Publisher;
import com.example.alleywayalliancelms.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    private final HoldService holdService;

    private final PatronAccountService patronAccountService;

    private final BookService bookService;


    @GetMapping("/catalog/book/{id}")
    public String getBookCopies(@PathVariable Long id,
                                Model model) {
        model.addAttribute("bookCopyList", bookCopyService.getBookCopiesByBookId(id));
        model.addAttribute("holdForm", new Hold());
        return "/copies/bookcopies";
    }

    @PostMapping("/catalog/book/{id}")
    public String addNewHold(@PathVariable Long id,
                             @ModelAttribute("holdForm") Hold holdForm,
                             @RequestParam(required = true, defaultValue = "") Long copyId,
                             Authentication authentication,
                             RedirectAttributes attributes) {

        if (holdService.saveHold(holdForm, copyId, authentication.getName())) {
            attributes.addFlashAttribute("copyId", copyId);
            return "redirect:/catalog/success";
        } else {
            attributes.addFlashAttribute("copyId", copyId);
            return "redirect:/catalog/{id}/denied";
        }
    }

    @GetMapping("/catalog/success")
    public String showSuccessBookCopyPage(ModelMap model,
                                          @ModelAttribute("copyId") Object copyId) {
        model.addAttribute("copyIdAttr", String.valueOf(copyId));
        return "/catalog/success";
    }

    @GetMapping("catalog/{id}/denied")
    public String showDeniedBookCopyPage(Model model,
                                         @PathVariable Long id) {
        model.addAttribute("bookId", id);
        return "/catalog/denied";
    }

    @PostMapping("catalog/{id}/denied")
    public String getDeniedBookCopyPage(@RequestParam(required = true, defaultValue = "") String action,
                                        Authentication authentication,
                                        @PathVariable Long id) throws BookNotFoundException {
        if (action.equals("enlist")) {
            if (patronAccountService.checkIfBookAlreadyInWaitlist(id, authentication.getName())) {
                return "redirect:/catalog";
            }
            patronAccountService.saveBookToWaitList(id, authentication.getName());
        }
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/addcopy")
    public String addBookCopyPage(Model model, @ModelAttribute("formErrors") String formErrors) {
        model.addAttribute("bookCopyForm", new BookCopy());
        model.addAttribute("publisherForm", new Publisher());
        model.addAttribute("publisherList", bookCopyService.getPublishers());
        model.addAttribute("bookList", bookService.getListOfAllBooks());
        return "copies/addCopy";
    }

    @PostMapping("/catalog/addcopy")
    public String postBookCopyPage(@Valid @ModelAttribute("bookCopyForm") BookCopy bookCopy,
                                   BindingResult bindingResult,
                                   Model model,
                                   @ModelAttribute("publisherForm") Publisher publisher,
                                   @RequestParam(required = true, defaultValue = "") Long publisherId,
                                   @RequestParam(required = true, defaultValue = "false") Boolean action,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formErrors", "В форме имеются ошибки!");
            return "redirect:/catalog/addcopy";
        }

        if (action) {
            bookCopyService.saveWithInternalPublisher(bookCopy, publisherId);
        } else {
            bookCopyService.saveWithPublisher(bookCopy, publisher);
        }
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/deletecopy")
    public String showDeleteCopyPage() {
        return "copies/deleteBookCopy";
    }

    @PostMapping("/catalog/deletecopy")
    public String postDeleteCopyPage(@RequestParam(name = "bookCopyId") Long bookCopyId,
                                     RedirectAttributes redirectAttributes) {
        BookCopy redirectCopy = bookCopyService.getBookCopyById(bookCopyId);

        redirectAttributes.addFlashAttribute("redirectCopy", redirectCopy);

        return "redirect:/catalog/copydeleteconfirm";
    }

    @GetMapping("/catalog/copydeleteconfirm")
    public String showCopyDeleteConfirm(@ModelAttribute(name = "redirectCopy") BookCopy redirectCopy,
                                        Model model) {
        model.addAttribute("redirectCopy", redirectCopy);
        return "copies/deleteCopyConfirm";
    }

    @PostMapping("/catalog/copydeleteconfirm")
    public String postCopyDeleteConfirm(@RequestParam(required = true, defaultValue = "") Long bookCopyId,
                                        @RequestParam(required = true, defaultValue = "") String action) {
        if (action.equals("delete")) {
            bookCopyService.deleteBookCopy(bookCopyId);
        }
        return "redirect:/catalog";
    }


}
