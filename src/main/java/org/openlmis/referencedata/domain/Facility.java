package org.openlmis.referencedata.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "facilities", schema = "referencedata")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Facility extends BaseEntity {

  public static final String TEXT = "text";

  @Column(nullable = false, unique = true, columnDefinition = TEXT)
  @Getter
  @Setter
  private String code;

  @Column(columnDefinition = TEXT)
  @Getter
  @Setter
  private String name;

  @Column(columnDefinition = TEXT)
  @Getter
  @Setter
  private String description;

  @ManyToOne
  @JoinColumn(name = "geographiczoneid", nullable = false)
  @Getter
  @Setter
  private GeographicZone geographicZone;

  @ManyToOne
  @JoinColumn(name = "typeid", nullable = false)
  @Getter
  @Setter
  private FacilityType type;

  @ManyToOne
  @JoinColumn(name = "operatedbyid")
  @Getter
  @Setter
  private FacilityOperator operator;

  @Column(nullable = false)
  @Getter
  @Setter
  private Boolean active;

  @Getter
  @Setter
  private Date goLiveDate;

  @Getter
  @Setter
  private Date goDownDate;

  @Column(columnDefinition = TEXT)
  @Getter
  @Setter
  private String comment;

  @Column(nullable = false)
  @Getter
  @Setter
  private Boolean enabled;

  @Getter
  @Setter
  private Boolean openLmisAccessible;

  @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Getter
  @Setter
  private Set<SupportedProgram> supportedPrograms;

  private Facility() {

  }

  public Facility(String code) {
    this.code = code;
  }

  /**
   * Equal by a Facility's code.
   *
   * @param other the other Facility
   * @return true if the two Facilities' {@link Code} are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Facility)) {
      return false;
    }

    Facility facility = (Facility) other;
    return code.equals(facility.getCode());
  }

  @Override
  public int hashCode() {
    return code.hashCode();
  }

  /**
   * Creates new facility object based on data from {@link Importer}
   *
   * @param importer instance of {@link Importer}
   * @return new instance of facility.
   */
  public static Facility newFacility(Importer importer) {
    Facility facility = new Facility();
    facility.setId(importer.getId());
    facility.setCode(importer.getCode());
    facility.setName(importer.getName());
    facility.setDescription(importer.getDescription());

    if (null != importer.getGeographicZone()) {
      facility.setGeographicZone(GeographicZone.newGeographicZone(importer.getGeographicZone()));
    }

    if (null != importer.getType()) {
      facility.setType(FacilityType.newFacilityType(importer.getType()));
    }

    if (null != importer.getOperator()) {
      facility.setOperator(FacilityOperator.newFacilityOperator(importer.getOperator()));
    }

    facility.setActive(importer.getActive());
    facility.setGoLiveDate(importer.getGoLiveDate());
    facility.setGoDownDate(importer.getGoDownDate());
    facility.setComment(importer.getComment());
    facility.setEnabled(importer.getEnabled());
    facility.setOpenLmisAccessible(importer.getOpenLmisAccessible());

    if (null != importer.getSupportedPrograms()) {
      Set<SupportedProgram> supportedPrograms = importer.getSupportedPrograms()
          .stream()
          .map(Program::newProgram)
          .map(program -> {
            SupportedProgram supportedProgram = new SupportedProgram();
            supportedProgram.setFacility(facility);
            supportedProgram.setProgram(program);

            return supportedProgram;
          })
          .collect(Collectors.toSet());

      facility.setSupportedPrograms(supportedPrograms);
    }

    return facility;
  }

  /**
   * Exports current state of facility object.
   *
   * @param exporter instance of {@link Exporter}
   */
  public void export(Exporter exporter) {
    exporter.setId(id);
    exporter.setCode(code);
    exporter.setName(name);
    exporter.setDescription(description);

    if (null != geographicZone) {
      exporter.setGeographicZone(geographicZone);
    }

    if (null != type) {
      exporter.setType(type);
    }

    if (null != operator) {
      exporter.setOperator(operator);
    }

    exporter.setActive(active);
    exporter.setGoLiveDate(goLiveDate);
    exporter.setGoDownDate(goDownDate);
    exporter.setComment(comment);
    exporter.setEnabled(enabled);
    exporter.setOpenLmisAccessible(openLmisAccessible);

    if (null != supportedPrograms) {
      exporter.setSupportedPrograms(supportedPrograms);
    }
  }

  public interface Exporter {

    void setId(UUID id);

    void setCode(String code);

    void setName(String name);

    void setDescription(String description);

    void setGeographicZone(GeographicZone geographicZone);

    void setType(FacilityType type);

    void setOperator(FacilityOperator operator);

    void setActive(Boolean active);

    void setGoLiveDate(Date goLiveDate);

    void setGoDownDate(Date goDownDate);

    void setComment(String comment);

    void setEnabled(Boolean enabled);

    void setOpenLmisAccessible(Boolean openLmisAccessible);

    void setSupportedPrograms(Set<SupportedProgram> supportedPrograms);

  }

  public interface Importer {

    UUID getId();

    String getCode();

    String getName();

    String getDescription();

    GeographicZone.Importer getGeographicZone();

    FacilityType.Importer getType();

    FacilityOperator.Importer getOperator();

    Boolean getActive();

    Date getGoLiveDate();

    Date getGoDownDate();

    String getComment();

    Boolean getEnabled();

    Boolean getOpenLmisAccessible();

    Set<Program.Importer> getSupportedPrograms();

  }
}
