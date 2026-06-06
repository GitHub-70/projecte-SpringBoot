package com.cy.pj.idempotent.example;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface FundRedeemService {
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    void redeemWithAtomicOperation(FundRedeemRequest request);
}
