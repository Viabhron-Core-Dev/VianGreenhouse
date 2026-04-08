import { useState, useEffect } from 'react';
import { VGHNote, VGHQuickPrompt } from '../types';
import { vghStorage } from '../lib/vghStorage';

export function useVGHStorage() {
  const [notes, setNotes] = useState<VGHNote[]>([]);
  const [prompts, setPrompts] = useState<VGHQuickPrompt[]>([]);

  useEffect(() => {
    setNotes(vghStorage.getNotes());
    setPrompts(vghStorage.getPrompts());

    // Listen for storage changes (for multi-tab support)
    const handleStorage = () => {
      setNotes(vghStorage.getNotes());
      setPrompts(vghStorage.getPrompts());
    };

    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, []);

  const addNote = (note: Omit<VGHNote, 'id' | 'createdAt' | 'updatedAt'>) => {
    const now = Date.now();
    const newNote: VGHNote = {
      ...note,
      id: crypto.randomUUID(),
      createdAt: now,
      updatedAt: now,
    };
    vghStorage.saveNote(newNote);
    setNotes(vghStorage.getNotes());
    return newNote;
  };

  const updateNote = (note: VGHNote) => {
    const updatedNote = { ...note, updatedAt: Date.now() };
    vghStorage.saveNote(updatedNote);
    setNotes(vghStorage.getNotes());
  };

  const deleteNote = (id: string) => {
    vghStorage.deleteNote(id);
    setNotes(vghStorage.getNotes());
  };

  const addPrompt = (prompt: Omit<VGHQuickPrompt, 'id' | 'sortOrder'>) => {
    const currentPrompts = vghStorage.getPrompts();
    const newPrompt: VGHQuickPrompt = {
      ...prompt,
      id: crypto.randomUUID(),
      sortOrder: currentPrompts.length,
    };
    vghStorage.savePrompt(newPrompt);
    setPrompts(vghStorage.getPrompts());
    return newPrompt;
  };

  const updatePrompt = (prompt: VGHQuickPrompt) => {
    vghStorage.savePrompt(prompt);
    setPrompts(vghStorage.getPrompts());
  };

  const deletePrompt = (id: string) => {
    vghStorage.deletePrompt(id);
    setPrompts(vghStorage.getPrompts());
  };

  return {
    notes,
    prompts,
    addNote,
    updateNote,
    deleteNote,
    addPrompt,
    updatePrompt,
    deletePrompt,
  };
}
