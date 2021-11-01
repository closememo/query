package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.LineDeltaConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT d.id, d.document_id, d.document_version, d.line_deltas, d.created_at FROM differences d")
@Synchronize({"differences"})
public class DifferenceDTO implements Serializable {

  @Id
  private String id;
  private String documentId;
  private long documentVersion;
  @Convert(converter = LineDeltaConverter.class)
  private List<LineDelta> lineDeltas;
  private ZonedDateTime createdAt;

  @Schema(name = "DifferenceDTO_LineDelta")
  @Getter
  public static class LineDelta implements Serializable {

    private DeltaType deltaType;
    private LineChunk source;
    private LineChunk target;
  }

  @Schema(name = "DifferenceDTO_LineChunk")
  @Getter
  public static class LineChunk implements Serializable {

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
