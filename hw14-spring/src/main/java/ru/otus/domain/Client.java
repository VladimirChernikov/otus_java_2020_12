package ru.otus.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
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
import ru.otus.domain.events.ClientCompleteEvent;
import ru.otus.domain.exceptions.AppDomainException;

@Table("client")
@Data
@Setter(AccessLevel.NONE)
@Builder(toBuilder = true, builderClassName = "BuilderWithValidation", buildMethodName = "build")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Client extends AbstractAggregateRoot<Client> implements Persistable<Long> {
    @Id
    // @NonNull
    Long id;
    @Transient
    @Builder.Default
    boolean isNew = false;
    @Version
    long version;

    @NonNull
    String name;

    @MappedCollection(idColumn = "client_id")
    Set<Phone> phones = new HashSet<>();

    @MappedCollection(idColumn = "client_id")
    Address address;


    public Client complete() {
        registerEvent( new ClientCompleteEvent(this.id) );
        return this;
    }


    public static class BuilderWithValidation {
        public BuilderWithValidation name(String name) {
            if ( name != null ) {
                if ( !name.substring(0,1).equals(name.substring(0,1).toUpperCase()) ) {
                    throw new AppDomainException( String.format( "The first letter of the client's name should be capital. Found: %s", name ));
                }
            }
            this.name = name;
            return this;
        }

        public BuilderWithValidation phones(Set<Phone> phones) {
            if ( phones != null ) {
                for ( var phone : phones )  {
                    String number = phone.getNumber();
                    if (number != null) {
                        if ( !number.matches("(7|8)-\\d{3}-\\d{3}-\\d{2}-\\d{2}") ) {
                            throw new AppDomainException( String.format( "The phone number should match the pattern (7|8)-999-999-99-99. Found: %s", number ));
                        }
                    }
                }
            }
            this.phones = phones;
            return this;
        }
    }

}
