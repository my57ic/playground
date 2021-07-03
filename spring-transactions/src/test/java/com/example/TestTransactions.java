package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(properties = {
        "logging.level.org.springframework.transaction.TransactionInterceptor=TRACE",
        "logging.level.org.springframework.jdbc.support.JdbcTransactionManager=DEBUG"})
class TestTransactions {


    @Autowired
    App.MainService mainService;

    @Test
    @DisplayName("Rollback main transaction on exception in nested REQUIRED propagation")
    void rollbackMainTransactionRequires() {
        assertThrows(RuntimeException.class, () -> mainService.executeWithNestedException(Propagation.REQUIRED));
    }

    @Test
    @DisplayName("No rollback for main transaction on exception in nested REQUIRES_NEW propagation")
    void noRollbackForMainTransactionRequiresNew() {
        assertDoesNotThrow(() -> mainService.executeWithNestedException(Propagation.REQUIRES_NEW));
    }

    @Test
    @DisplayName("Rollback main transaction on exception in nested MANDATORY propagation")
    void rollbackMainTransactionMandatory() {
        assertThrows(RuntimeException.class, () -> mainService.executeWithNestedException(Propagation.MANDATORY));
    }

    @Test
    @DisplayName("No rollback for main transaction on exception in nested NESTED propagation")
    void noRollbackForMainTransactionNested() {
        assertDoesNotThrow(() -> mainService.executeWithNestedException(Propagation.NESTED));
    }

    @Test
    @DisplayName("No rollback for main transaction on exception in nested NOT_SUPPORTED propagation")
    void noRollbackForMainTransactionSupports() {
        assertThrows(UnexpectedRollbackException.class, () -> mainService.executeWithNestedException(Propagation.SUPPORTS));
    }

    @Test
    @DisplayName("No rollback for main transaction on exception in nested NOT_SUPPORTED propagation")
    void noRollbackForMainTransactionNotSupported() {
        assertDoesNotThrow(() -> mainService.executeWithNestedException(Propagation.NOT_SUPPORTED));
    }

    @Test
    @DisplayName("No rollback for main transaction on exception in nested NOT_SUPPORTED propagation")
    void rollbackForMainTransactionNever() {
        assertDoesNotThrow(() -> mainService.executeWithNestedException(Propagation.NEVER));
    }

    @Test
    @DisplayName("REQUIRES_NEW propagation")
    void mainTransactionRequiresNew() {
        mainService.execute(Propagation.REQUIRES_NEW);
    }

    @Test
    @DisplayName("NESTED propagation")
    void mainTransactionNested() {
        mainService.execute(Propagation.NESTED);
    }
}
