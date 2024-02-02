package ru.netology.estore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.estore.dto.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val firstName:String,
    val username:String,
    val password:String,
    val token:String
) {
    fun toDto() = User(id, firstName, username, password, token)

    companion object {
        fun fromDto(dto: User) =
            UserEntity(dto.id, dto.firstName, dto.username, dto.password, dto.token)
    }
}
