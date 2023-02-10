package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Checkout;
import com.example.alleywayalliancelms.Model.PatronAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Checkout findCheckoutByBookId(Long copyBookId);

    Checkout findCheckoutByBookIdAndEndDate(Long copyBookId, Date enddate);

    @Query(value = "SELECT count(*) FROM checkout WHERE book_copy_id = :copyId AND is_returned = false", nativeQuery = true)
    Long checkIfCopyIsAvailible(@Param("copyId") Long copyId);

    @Query(value = "SELECT count(*) FROM checkout WHERE is_returned = false AND book_copy_id = :copyId AND patron_account_id = :userId", nativeQuery = true)
    Long checkIfUserAlreadyHaveAnotherCopyOfBookQuery(@Param("copyId") Long copyId, @Param("userId") String userId);

    List<Checkout> findCheckoutByPatronAccountId(String accountId);

    Checkout findCheckoutByIsReturnedAndPatronAccountIdAndStartDate(Boolean isReturned, String accountId, Date startDate);

    Checkout findCheckoutByStartDateAndPatronAccountId(Date startDate, String accountId);

    List<Checkout> findCheckoutByIsReturnedAndPatronAccount(Boolean isReturned, PatronAccount patronAccount);


}