package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.LineDeltaConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT d.id, d.owner_id, d.document_id, d.document_version, d.line_deltas, d.created_at FROM differences d")
@Synchronize({"differences"})
public class DifferenceDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  private String documentId;
  private long documentVersion;
  @Convert(converter = LineDeltaConverter.class)
  private List<LineDelta> lineDeltas;
  private ZonedDateTime createdAt;

  @Schema(name = "DifferenceDTO_LineDelta")
  @Getter
  public static class LineDelta implements Serializable {

    private DeltaType type;
    private Line source;
    private Line target;
    private List<ChangePatch> changePatches;
  }

  @Schema(name = "DifferenceDTO_LineChunk")
  @Getter
  public static class Line implements Serializable {

    private int position;
    private String value;
  }

  @Schema(name = "DifferenceDTO_ChangePatch")
  @Getter
  public static class ChangePatch implements Serializable {

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
