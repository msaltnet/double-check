package net.msalt.doublecheck.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "bunch")
data class Bunch(
        @ColumnInfo(name = "title") var title: String = "",
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "category") var category: String = "")
