package com.example.alleywayalliancelms.repository;

import com.example.alleywayalliancelms.model.Checkout;
import com.example.alleywayalliancelms.model.PatronAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Checkout findCheckoutByBookIdAndEndDate(Long copyBookId, LocalDate endDate);

    @Query(value = "SELECT count(*) FROM checkout WHERE book_copy_id = :copyId AND is_returned = false", nativeQuery = true)
    Long checkIfCopyIsAvailible(@Param("copyId") Long copyId);

    @Query(value = "SELECT count(*) FROM checkout WHERE is_returned = false AND book_copy_id = :copyId AND patron_account_id = :userId", nativeQuery = true)
    Long checkIfUserAlreadyHaveAnotherCopyOfBookQuery(@Param("copyId") Long copyId, @Param("userId") String userId);

    List<Checkout> findCheckoutByPatronAccountId(String accountId);

    Checkout findCheckoutByIsReturnedAndPatronAccountIdAndStartDate(Boolean isReturned, String accountId, LocalDateTime startDate);

    Checkout findCheckoutByStartDateAndPatronAccountId(LocalDateTime startDate, String accountId);

    List<Checkout> findCheckoutByIsReturnedAndPatronAccount(Boolean isReturned, PatronAccount patronAccount);


}