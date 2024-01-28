package ru.netology.estore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.estore.dto.DataHistory

@Entity
class DataHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val login:String,
    val sumOrder:Double,
    val pickUp:Boolean,
    val dateTime: String
) {
        fun toDto() = DataHistory(id, login, sumOrder, pickUp, dateTime)

        companion object {
            fun fromDto(dto: DataHistory) =
                DataHistoryEntity(dto.id, dto.login, dto.sumOrder, dto.pickUp, dto.dateTime)
        }
}

fun List<DataHistoryEntity>.toDto(): List<DataHistory> = map(DataHistoryEntity::toDto)

fun List<DataHistory>.toEntity(): List<DataHistoryEntity> = map(DataHistoryEntity::fromDto)