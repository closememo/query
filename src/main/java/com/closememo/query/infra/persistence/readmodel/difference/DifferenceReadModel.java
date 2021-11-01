package com.closememo.query.infra.persistence.readmodel.difference;

import com.closememo.query.infra.converter.LineDeltaReadModelConverter;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "differences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class DifferenceReadModel {

  @Id
  @Column(unique = true, nullable = false)
  private String id;
  @Column(nullable = false)
  private String documentId;
  @Column(nullable = false)
  private long documentVersion;
  @Column(nullable = false, columnDefinition = "JSON")
  @Convert(converter = LineDeltaReadModelConverter.class)
  private List<LineDelta> lineDeltas;
  @Column(nullable = false)
  private ZonedDateTime createdAt;

  @Builder
  public DifferenceReadModel(String id, String documentId, long documentVersion,
      List<LineDelta> lineDeltas, ZonedDateTime createdAt) {
    this.id = id;
    this.documentId = documentId;
    this.documentVersion = documentVersion;
    this.lineDeltas = lineDeltas;
    this.createdAt = createdAt;
  }

  @Getter
  public static class LineDelta {

    private DeltaType deltaType;
    private LineChunk source;
    private LineChunk target;
  }

  @Getter
  public static class LineChunk {

    private int position;
    private List<String> lines;
  }

  public enum DeltaType {
    CHANGE,
    DELETE,
    INSERT,
    EQUAL
  }
}
