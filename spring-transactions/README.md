# Transactions

Spring transactions behaviour in case of an exception in nested transaction.

| Nested transaction  | Rollback on main transaction  | JdbcTransactionManager |
|---|---|---|
|REQUIRED   | Yes | Participating in existing transaction, Participating transaction failed - marking existing transaction as rollback-only
|MANDATORY   | Yes  | Participating in existing transaction, Participating transaction failed - marking existing transaction as rollback-only
|SUPPORTS   | Yes  | Participating in existing transaction, Participating transaction failed - marking existing transaction as rollback-only
|REQUIRES NEW   | No  | Rolling back nested transaction
|NESTED   | No  | Rolling back transaction to savepoint
|NOT SUPPORTED   | No  | Should roll back transaction but cannot - no transaction available
|NEVER   | No  | Existing transaction found for transaction marked with propagation 'never'


# Difference between NESTED and REQUIRES_NEW transactions

REQURIES_NEW acquires a new physical connection to create a new transaction, because it's impossible to start a new transaction on the same connection when
there is already a transaction in progress.

```puml
@startuml
    TransactionManager -> TransactionManager: create new transaction A
    TransactionManager -> DataSource: acquire new connection A
    DataSource -> Connection_A ** : create
    TransactionManager <-- DataSource: return connection
    TransactionManager -> TransactionManager: suspend transaction A
    
    group Second transaction
    TransactionManager -> TransactionManager: create transaction B
    TransactionManager -> DataSource: acquire new connection B
    DataSource -> Connection_B **: create
    TransactionManager <-- DataSource: return connection
    TransactionManager -> Connection_B: commit
    TransactionManager -> DataSource: release(Connection_B)
    DataSource -> Connection_B !!:
    end
    
    TransactionManager -> TransactionManager: resume suspended transaction A
    TransactionManager -> Connection_A: commit
    TransactionManager -> DataSource: release(Connection_A)
    DataSource -> Connection_A !!:
@enduml
```


NESTED on the other hand uses [JDBC Savepoint](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html) to mark a point that we can rollback to in case of an exception.
Main transaction doesn't have to be rolled back though.
Also NESTED transaction is committed at the end of the outer transaction.


```puml
@startuml
    TransactionManager -> TransactionManager: create new transaction A
    TransactionManager -> DataSource: acquire new connection
    DataSource -> Connection ** : create
    TransactionManager <-- DataSource: return connection
    
    group Nested transaction
    TransactionManager -> TransactionManager:  create nested transaction B
    TransactionManager -> Connection:  create transaction save point
    TransactionManager -> Connection: release transaction savepoint
    end
    
    TransactionManager -> Connection: commit
    TransactionManager -> DataSource: release(Connection)
    DataSource -> Connection_A !!:
@enduml
```



















