package ru.netology.estore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.estore.dto.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name:String,
    val login:String,
    val password:String,
    val token:String
) {
    fun toDto() = User(id, name, login,password, token)

    companion object {
        fun fromDto(dto: User) =
            UserEntity(dto.id, dto.name, dto.login, dto.password, dto.token)

    }
}
