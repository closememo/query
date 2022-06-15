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
  @Column(nullable = false, columnDefinition = "VARCHAR(24)")
  private String ownerId;
  @Column(nullable = false)
  private String documentId;
  @Column(nullable = false)
  private long documentVersion;
  @Column(nullable = false, columnDefinition = "JSON")
  @Convert(converter = LineDeltaReadModelConverter.class)
  private List<LineDelta> lineDeltas;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  // read-only
  private int inserted;
  private int deleted;
  private int changed;

  @Builder(toBuilder = true)
  public DifferenceReadModel(String id, String ownerId, String documentId, long documentVersion,
      List<LineDelta> lineDeltas, ZonedDateTime createdAt, int inserted, int deleted, int changed) {
    this.id = id;
    this.ownerId = ownerId;
    this.documentId = documentId;
    this.documentVersion = documentVersion;
    this.lineDeltas = lineDeltas;
    this.createdAt = createdAt;
    this.inserted = inserted;
    this.deleted = deleted;
    this.changed = changed;
  }

  @Getter
  public static class LineDelta {

    private DeltaType type;
    private Line source;
    private Line target;
    private List<ChangePatch> changePatches;
  }

  @Getter
  public static class Line {

    private int position;
    private String value;
  }

  @Getter
  public static class ChangePatch {

    private DeltaType type;
    private String value;
    private String changed;
  }

  public enum DeltaType {
    CHANGE,
    DELETE,
    INSERT,
    EQUAL
  }
}
