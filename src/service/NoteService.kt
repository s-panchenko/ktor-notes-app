package me.spanchenko.service

import dao.Note
import dao.Notes
import dao.toDto
import me.spanchenko.dto.NoteDto
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

interface NoteService {
    suspend fun findAll(): List<NoteDto>
    suspend fun findById(id: Long): NoteDto
    suspend fun save(note: NoteDto): NoteDto
    suspend fun update(id: Long, note: NoteDto): NoteDto
    suspend fun delete(id: Long)
}

class NoteServiceImpl : NoteService {
    override suspend fun findAll() = newSuspendedTransaction {
        Note.all().orderBy(Notes.id to SortOrder.ASC).map(Note::toDto)
    }

    override suspend fun findById(id: Long) = newSuspendedTransaction {
        Note[id].toDto()
    }

    override suspend fun save(note: NoteDto) = transaction {
        Note.new {
            this.title = note.title
            this.content = note.content
        }
    }.toDto()

    override suspend fun update(id: Long, note: NoteDto) = transaction {
        Note[id].apply {
            this.title = note.title
            this.content = note.content
        }
    }.toDto()

    override suspend fun delete(id: Long) = transaction {
        Note[id].delete()
    }
}
