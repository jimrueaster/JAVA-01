package com.example.demo;

import com.google.common.base.Objects;
import lombok.Getter;

@Getter
abstract public class IdentifiedValueObject {

    protected String id;

    protected IdentifiedValueObject(String anId) {
        id = anId;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;
        IdentifiedValueObject that = (IdentifiedValueObject) aO;
        return Objects.equal(id, that.id);
    }

}
