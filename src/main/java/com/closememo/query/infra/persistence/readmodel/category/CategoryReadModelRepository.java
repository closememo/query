package com.closememo.query.infra.persistence.readmodel.category;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReadModelRepository extends JpaRepository<CategoryReadModel, String> {

  Stream<CategoryReadModel> findAllByOwnerId(String ownerId);

  Stream<CategoryReadModel> findAllByIdIn(Iterable<String> categoryIds);
}
