package dao

import me.spanchenko.dto.NoteDto
import me.spanchenko.dto.ResponseData
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SizedIterable

object Notes : LongIdTable() {
    val title = varchar("title", 50)
    val content = varchar("content", 255)
}

class Note(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Note>(Notes)

    var title by Notes.title
    var content by Notes.content
}

fun Note.toDto() = NoteDto(id.value, title, content)
