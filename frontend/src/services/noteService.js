import api from './api';

export const noteService = {
  createNote: (title, content, parentId) =>
    api.post('/notes', { title, content, parentId }),

  getNote: (id) =>
    api.get(`/notes/${id}`),

  getRootNotes: () =>
    api.get('/notes'),

  getNoteChildren: (id) =>
    api.get(`/notes/${id}/children`),

  updateNote: (id, title, content) =>
    api.put(`/notes/${id}`, { title, content }),

  exportMarkdown: (id) =>
      api.get(`/notes/${id}/export/markdown`, { responseType: 'blob' }),

  searchNotes: (query) =>
      api.get(`/notes/search`, { params: { query } }),

  deleteNote: (id) =>
    api.delete(`/notes/${id}`),
};