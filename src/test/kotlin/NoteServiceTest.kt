import org.junit.Assert.*
import org.junit.Test

class NoteServiceTest {
    @Test
    fun getNoteByIdNoZero() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "Don't forget", 202020, "private", true, false))
        val newNote:Note = NoteService.getNoteById(note.id)
        val result = note === newNote
        assertTrue(result)
    }

    @Test
    fun deleteCommentShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 23, "Note", "To do", 202021, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val result = NoteService.deleteComment(note.id, comment.id)
        assertTrue(result)
    }
    @Test
    fun editCommentShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 26, "Memo", "Don't forget", 212020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(50, 202120, "New text", 450, 5, true, false))
        val newComment = comment.copy(text = "New text")
        NoteService.editComment(note.id, comment.id, newComment)
        assertTrue(newComment in note.comments)
    }
    @Test(expected = NoteNotFoundException::class)
    fun restoreCommentShodThrowNoteNotFoundException() {
        val note = NoteService.addNote(Note(0, 33, "Memo", "NewText1", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(14, 252120, "NewComment1", 470, 0, true, false))
        val isDeleted = NoteService.deleteComment(note.id, comment.id)
        val falseNoteId = note.id + 1
        if (isDeleted) NoteService.restoreComment(falseNoteId, comment.id)
    }
    @Test
    fun restoreCommentNoZero() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText2", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val isDeleted = NoteService.deleteComment(note.id, comment.id)
        if (isDeleted) {
            val result = NoteService.restoreComment(note.id, comment.id)
            assertTrue(result)
        }
    }


    @Test
    fun addNoteNoZero() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText2", 202020, "private", true, false))
        assertNotEquals(0, note.id)
    }

    @Test
    fun updateNoteShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText34", 202020, "private", true, false))
        val exists = NoteService.editNote(note.id, note.copy(text = "Note"))
        assertTrue(exists)
    }

    @Test(expected = NoteNotFoundException::class)
    fun updateNoteShouldThrowNoteNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText67", 202020, "private", true, false))
        val falseNoteId = note.id + 1
        NoteService.editNote(falseNoteId, note.copy(text = "another text"))
    }

    @Test
    fun commentShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText9", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        NoteService.getNotes().last().id
        val returnComment = NoteService.getComments(note.id).last()
        assertEquals(comment, returnComment)
    }

    @Test(expected = NoteNotFoundException::class)
    fun commentShouldThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText07", 202020, "private", true, false))
        val falsePostId = note.id + 1
        val comment = Comment(34, 232020, "Done", 456, 0, true, false)
        NoteService.createComment(falsePostId, comment)
    }

    @Test
    fun deleteNoteShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText090", 202020, "private", true, false))
        val result = NoteService.deleteNote(note.id)
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteShouldThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText657", 202020, "private", true, false))
        val noteId = note.id
        NoteService.deleteNote(noteId)
        NoteService.deleteNote(noteId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteCommentShouldThrowNoteNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText086", 202020, "private", true, false))
        val falseNoteId = note.id + 1
        val comment = NoteService.createComment(falseNoteId, Comment(34, 232020, "Done", 456, 0, true, false))
        NoteService.deleteComment(note.id, comment.id)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteCommentShouldThrowCommentNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText90", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val falseCommentId = comment.id + 1
        NoteService.deleteComment(note.id, falseCommentId)
    }

    @Test
    fun editNoteShouldNotThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText540", 202020, "private", true, false))
        val newNote = note.copy(text = "New text")
        NoteService.editNote(note.id, newNote)
        assertTrue(newNote in NoteService.getNotes())
    }

    @Test(expected = NoteNotFoundException::class)
    fun editNoteShouldThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText430", 202020, "private", true, false))
        val newNote = note.copy(text = "New text")
        val falseNoteId = note.id + 1
        NoteService.editNote(falseNoteId, newNote)
    }

    @Test(expected = NoteNotFoundException::class)
    fun editCommentShouldThrowNoteNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText79", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val newComment = comment.copy(text = "New text")
        val falseNoteId = note.id + 1
        NoteService.editComment(falseNoteId, comment.id, newComment)
    }

    @Test(expected = CommentNotFoundException::class)
    fun editCommentShouldThrowCommentNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText456", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val newComment = comment.copy(text = "New text")
        val falseCommentId = comment.id + 1
        NoteService.editComment(note.id, falseCommentId, newComment)
    }

    @Test
    fun getNoteNoZero() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NextText896", 202020, "private", true, false))
        val notes = NoteService.getNotes()
        val result = note === notes.last()
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getNoteByIdShodThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText908", 202020, "private", true, false))
        val falseNoteId = note.id + 1
        NoteService.getNoteById(falseNoteId)
    }

    @Test
    fun getCommentNoZero() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText78", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val comments = NoteService.getComments(note.id)
        val result = comment === comments.last()
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getCommentShodThrow() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText56", 202020, "private", true, false))
        val falseNoteId = note.id + 1
        NoteService.getComments(falseNoteId)
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreCommentShodThrowCommentNotFoundException() {
        val note = NoteService.addNote(Note(0, 23, "Memo", "NewText099", 202020, "private", true, false))
        val comment = NoteService.createComment(note.id, Comment(34, 232020, "Done", 456, 0, true, false))
        val isDeleted = NoteService.deleteComment(note.id, comment.id)
        val falseCommentId = comment.id + 1
        if (isDeleted) NoteService.restoreComment(note.id, falseCommentId)
    }
}