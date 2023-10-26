data class Note (
    val id: Int = 0,
    val ownerId: Int,
    val title: String,
    val text: String,
    val date: Int,
    val privacyView: String,
    val canComment: Boolean,
    var isDeleted: Boolean,
    var comments: MutableList<Comment> = mutableListOf<Comment>()
)

data class Comment(
    val id: Int = 0,
    var date: Int,
    val text: String,
    var ownerId: Int,
    val count: Int = 0,
    val canPost: Boolean = true,
    var isDeleted: Boolean
)

object NoteService {
    private var notes = mutableListOf<Note>() //Изменяемый список для хранения записей
    private var comments = emptyArray<Comment>()
    private var nextId = 0 //Для нумерации записей в массиве

    fun addNote(note: Note): Note { //Создаёт новую заметку у текущего пользователя
        notes += note.copy(id = ++nextId) //Добавляет в список notes новую запись, присваивает ей id
        return notes.last() //Возвращает последний элемент списка notes
    }

    fun createComment(noteId: Int, comment: Comment): Comment { //Добавляет новый комментарий к заметке
        val note = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        note.comments += comment.copy(id =  ++nextId)
        return note.comments.last()
    }

    fun deleteNote(noteId: Int): Boolean { //Удаляет заметку текущего пользователя
        val note = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        note.isDeleted = true
        return true
    }

    fun deleteComment(noteId: Int, commentId: Int): Boolean { //Удаляет комментарий к заметке
        val note = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        val comment = note.comments.find {it.id == commentId && !it.isDeleted} ?: throw CommentNotFoundException("No comment with $commentId")
        comment.isDeleted = true
        return true
    }

    fun editNote(noteId: Int, note: Note): Boolean { //Редактирует заметку текущего пользователя
        val thisNote = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        notes[notes.indexOf(thisNote)] = note.copy(
            id = thisNote.id,
            ownerId = thisNote.ownerId,
            date = thisNote.date
        )
        return true
    }

    fun editComment(noteId: Int, commentId: Int, comment: Comment): Boolean { //Редактирует указанный комментарий у заметки
        val note = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        val thisComment = note.comments.find{it.id == commentId && !it.isDeleted} ?: throw CommentNotFoundException("No comment with $commentId")
        note.comments[note.comments.indexOf(thisComment)] = comment.copy(
            id = thisComment.id,
            ownerId = thisComment.ownerId,
            date = thisComment.date
        )
        return true
    }

    fun getNotes() = notes.filter { !it.isDeleted } //Возвращает список заметок, созданных пользователем

    fun getNoteById(noteId: Int) = //Возвращает заметку по её id
        notes.find{it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")

    fun getComments(noteId: Int): MutableList<Comment> { //Возвращает список комментариев к заметке
        val note = notes.find{it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        return note.comments.filter { !it.isDeleted }.toMutableList()
    }

    fun restoreComment(noteId: Int, commentId: Int): Boolean { //Восстанавливает удалённый комментарий
        val note = notes.find {it.id == noteId && !it.isDeleted} ?: throw NoteNotFoundException("No note with $noteId")
        val comment = note.comments.find{it.id == commentId && it.isDeleted} ?: throw CommentNotFoundException("No comment with $commentId")
        return true
    }
}

class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)

