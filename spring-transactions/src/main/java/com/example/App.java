package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootApplication
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Service
    public static class MainService {

        private final NestedService nestedService;

        MainService(NestedService nestedService) {
            this.nestedService = nestedService;
        }

        @Transactional
        public void execute(Propagation nestedPropagation) {
            try {
                logger.info("");
                logger.info("Propagation " + nestedPropagation);
                logger.info("");
                logger.info("Entering main service");
                logger.info("Active transaction: " + TransactionSynchronizationManager.getCurrentTransactionName());

                switch (nestedPropagation) {
                    case NESTED -> nestedService.nestedWithoutException();
                    case REQUIRES_NEW -> nestedService.requiresNewWithoutException();
                }
                logger.info("Exiting main service");
            } catch (Exception ex) {
                logger.error("exception: ", ex);
            }
        }

        @Transactional
        public void executeWithNestedException(Propagation nestedPropagation) {
            try {
                logger.info("");
                logger.info("Propagation " + nestedPropagation);
                logger.info("");
                logger.info("Entering main service");
                logger.info("Active transaction: " + TransactionSynchronizationManager.getCurrentTransactionName());

                switch (nestedPropagation) {
                    case REQUIRED -> nestedService.required();
                    case REQUIRES_NEW -> nestedService.requiresNew();
                    case MANDATORY -> nestedService.mandatory();
                    case NESTED -> nestedService.nested();
                    case SUPPORTS -> nestedService.supports();
                    case NOT_SUPPORTED -> nestedService.notSupported();
                    case NEVER -> nestedService.never();
                }

                logger.info("Exiting main service");
            } catch (Exception ex) {
                logger.error("exception: ", ex);
            }
        }
    }

    @Service
    static class NestedService {

        @Transactional(propagation = Propagation.REQUIRED)
        public void required() {
            execute(true);
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void requiresNew() {
            execute(true);
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void requiresNewWithoutException() {
            execute(false);
        }

        @Transactional(propagation = Propagation.NESTED)
        public void nested() {
            execute(true);
        }

        @Transactional(propagation = Propagation.NESTED)
        public void nestedWithoutException() {
            execute(false);
        }

        @Transactional(propagation = Propagation.MANDATORY)
        public void mandatory() {
            execute(true);
        }

        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        public void notSupported() {
            execute(true);
        }

        @Transactional(propagation = Propagation.SUPPORTS)
        public void supports() {
            execute(true);
        }

        @Transactional(propagation = Propagation.NEVER)
        public void never() {
            execute(true);
        }

        private void execute(boolean throwException) {
            logger.info("Entering nested service");
            logger.info("Active transaction: " + TransactionSynchronizationManager.getCurrentTransactionName());
            if (throwException)
            throw new RuntimeException("Exception in NestedService");
        }

    }
}
