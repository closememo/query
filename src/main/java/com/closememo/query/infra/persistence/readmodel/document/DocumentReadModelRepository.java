package com.closememo.query.infra.persistence.readmodel.document;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentReadModelRepository extends JpaRepository<DocumentReadModel, String> {

  long countByCategoryId(String categoryId);
}
