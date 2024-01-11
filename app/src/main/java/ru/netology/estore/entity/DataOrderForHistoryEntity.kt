package ru.netology.estore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.estore.dto.DataOrderForHistory
import ru.netology.estore.dto.User
import java.time.OffsetDateTime

@Entity
class DataOrderForHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val login:String,
    val sumOrder:Double,
    val pickUp:Boolean,
    val dateTime: String
) {
        fun toDto() = DataOrderForHistory(id, login, sumOrder, pickUp, dateTime)

        companion object {
            fun fromDto(dto: DataOrderForHistory) =
                DataOrderForHistoryEntity(dto.id, dto.login, dto.sumOrder, dto.pickUp, dto.dateTime)
        }
}

fun List<DataOrderForHistoryEntity>.toDto(): List<DataOrderForHistory> = map(DataOrderForHistoryEntity::toDto)

fun List<DataOrderForHistory>.toEntity(): List<DataOrderForHistoryEntity> = map(DataOrderForHistoryEntity::fromDto)