package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table("address")
@Data
@Setter(AccessLevel.NONE)
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Address implements Cloneable {
    @Id
    // @NonNull
    Long id;
    @Transient
    @Builder.Default
    boolean isNew = false;
    @Version
    long version;

    String street;

    @NonNull
    Long clientId;

}
