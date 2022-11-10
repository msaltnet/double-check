package net.msalt.doublecheck.data

import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "checkitem")
data class CheckItem(
        @ColumnInfo(name = "contents") var contents: String = "",
        @Ignore var contents_data: MutableLiveData<String> = MutableLiveData<String>(""),
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "bunchId") var bunchId: String = "",
        @ColumnInfo(name = "checked") var checked: Boolean = false,
)
