package net.msalt.doublecheck.data

import androidx.room.*

@Entity(tableName = "BunchWithCheckItem")
data class BunchWithCheckItem(
        @Embedded val user: Bunch,
        @Relation(
                parentColumn = "id",
                entityColumn = "bunchId"
        )
        val checkItems: List<CheckItem>
)
