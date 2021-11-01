package com.closememo.query.infra.persistence.readmodel.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountReadModelRepository extends JpaRepository<AccountReadModel, String> {

}
