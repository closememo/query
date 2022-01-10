package com.closememo.query.infra.persistence.readmodel.suggestion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionReadModelRepository extends JpaRepository<SuggestionReadModel, String> {

}
