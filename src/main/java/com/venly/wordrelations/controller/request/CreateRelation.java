package com.venly.wordrelations.controller.request;

import com.venly.wordrelations.enumeration.RelationType;
import jakarta.validation.constraints.NotEmpty;

public record CreateRelation(@NotEmpty String word, @NotEmpty String anotherWord, @NotEmpty RelationType relation) {
}
