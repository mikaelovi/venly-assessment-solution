package com.venly.wordrelations.enumeration;

import java.util.Locale;
import java.util.Objects;

public enum RelationType {
    SYNONYM, ANTONYM, RELATED;

    public static RelationType getMatching(final String relation) {
        if (Objects.isNull(relation)) return null;

        for (RelationType relationType : RelationType.values()) {
            if (relationType.name().toLowerCase(Locale.ROOT).equals(relation.toLowerCase(Locale.ROOT)))
                return relationType;
        }

        return null;
    }
}
