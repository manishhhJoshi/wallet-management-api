package com.sct.wallet.backend.specification;

import com.sct.wallet.backend.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionSpecification {

    // Transactions for given wallets
    public static Specification<Transaction> walletIn(List<UUID> walletIds) {
        return (root, query, cb) ->
                root.get("wallet").get("id").in(walletIds);
    }

    // Filter by date range
    public static Specification<Transaction> createdBetween(
            LocalDateTime from,
            LocalDateTime to
    ) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), from, to);
    }

    // Filter by references
    public static Specification<Transaction> referenceIn(List<String> references) {
        return (root, query, cb) ->
                root.get("reference").in(references);
    }
}
