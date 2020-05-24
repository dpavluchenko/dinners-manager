package com.pavliuchenko.dao.source;


import com.pavliuchenko.dao.function.StatementExecutor;
import com.pavliuchenko.dao.function.TransactionExecutor;

public interface DatabaseSource {
    <T> T executeQuery(String sql, StatementExecutor<T> executor);
    <T> T executeTransaction(TransactionExecutor<T> executor);
}
