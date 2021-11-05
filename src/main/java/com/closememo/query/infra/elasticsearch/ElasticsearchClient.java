package com.closememo.query.infra.elasticsearch;

import com.closememo.query.infra.elasticsearch.request.SearchPostByTagRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElasticsearchClient {

  private final RestHighLevelClient client;

  public ElasticsearchClient(RestHighLevelClient client) {
    this.client = client;
  }

  public List<String> searchPostIdsByTag(SearchPostByTagRequest searchPostByTagRequest) {
    log.debug("[ELASTICSEARCH] request: " + searchPostByTagRequest.getSearchRequest().source().toString());
    SearchResponse response;
    try {
      response = client.search(searchPostByTagRequest.getSearchRequest(), RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchClientException();
    }
    log.debug("[ELASTICSEARCH] response: " + response.toString());

    validateResponse(response);

    SearchHit[] hits = response.getHits().getHits();
    return Stream.of(hits).map(SearchHit::getSourceAsMap)
        .map(stringObjectMap -> stringObjectMap.get("id"))
        .map(String.class::cast)
        .collect(Collectors.toList());
  }

  private void validateResponse(SearchResponse response) {
    if (!RestStatus.OK.equals(response.status())) {
      throw new ElasticsearchClientException();
    }
  }
}
