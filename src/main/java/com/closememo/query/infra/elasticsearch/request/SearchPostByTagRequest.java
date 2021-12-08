package com.closememo.query.infra.elasticsearch.request;

import lombok.Getter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@Getter
public class SearchPostByTagRequest {

  private static final String[] INDEX_NAMES = new String[]{"post", "auto-tag"};

  private static final String FIELD_OWNER_ID = "ownerId";
  private static final String FIELD_TAGS = "tags";
  private static final String FILED_AUTO_TAGS = "autoTags";
  private static final String[] INCLUDE_FIELDS = new String[]{"id", "ownerId"};

  private final SearchRequest searchRequest;

  public SearchPostByTagRequest(String ownerId, String tag) {

    this.searchRequest = new SearchRequest(INDEX_NAMES)
        .source(new SearchSourceBuilder()
            .query(QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(FIELD_OWNER_ID, ownerId))
                .filter(QueryBuilders.boolQuery()
                    .should(QueryBuilders.termQuery(FIELD_TAGS, tag))
                    .should(QueryBuilders.termQuery(FILED_AUTO_TAGS, tag))))
            .fetchSource(INCLUDE_FIELDS, null)
            .from(0)
            .size(10));
  }
}
