package net.msalt.doublecheck.data

import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Bunch")
data class Bunch(
        @ColumnInfo(name = "title") var title: String = "",
        @Ignore var title_data: MutableLiveData<String> = MutableLiveData<String>(""),
        @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "category") var category: String = "")
