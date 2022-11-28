package net.msalt.doublecheck.data

import androidx.room.*

data class BunchWithCheckItem(
    @Embedded val bunch: Bunch,
    @Relation(
        parentColumn = "id",
        entityColumn = "bunchId"
    )
    val checkItems: List<CheckItem>
)
