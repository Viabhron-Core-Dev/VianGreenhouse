import { VGHNote, VGHQuickPrompt } from '../types';

const NOTES_KEY = 'vgh_notes';
const PROMPTS_KEY = 'vgh_prompts';
const SETTINGS_KEY = 'vgh_settings';

export const vghStorage = {
  // Notes
  getNotes: (): VGHNote[] => {
    const data = localStorage.getItem(NOTES_KEY);
    return data ? JSON.parse(data) : [];
  },
  saveNote: (note: VGHNote) => {
    const notes = vghStorage.getNotes();
    const index = notes.findIndex(n => n.id === note.id);
    if (index >= 0) {
      notes[index] = note;
    } else {
      notes.push(note);
    }
    localStorage.setItem(NOTES_KEY, JSON.stringify(notes));
  },
  deleteNote: (id: string) => {
    const notes = vghStorage.getNotes().filter(n => n.id !== id);
    localStorage.setItem(NOTES_KEY, JSON.stringify(notes));
  },

  // Quick Prompts
  getPrompts: (): VGHQuickPrompt[] => {
    const data = localStorage.getItem(PROMPTS_KEY);
    return data ? JSON.parse(data) : [];
  },
  savePrompt: (prompt: VGHQuickPrompt) => {
    const prompts = vghStorage.getPrompts();
    const index = prompts.findIndex(p => p.id === prompt.id);
    if (index >= 0) {
      prompts[index] = prompt;
    } else {
      prompts.push(prompt);
    }
    localStorage.setItem(PROMPTS_KEY, JSON.stringify(prompts));
  },
  deletePrompt: (id: string) => {
    const prompts = vghStorage.getPrompts().filter(p => p.id !== id);
    localStorage.setItem(PROMPTS_KEY, JSON.stringify(prompts));
  },

  // Settings
  getSetting: <T>(key: string, defaultValue: T): T => {
    const settings = JSON.parse(localStorage.getItem(SETTINGS_KEY) || '{}');
    return settings[key] !== undefined ? settings[key] : defaultValue;
  },
  setSetting: (key: string, value: any) => {
    const settings = JSON.parse(localStorage.getItem(SETTINGS_KEY) || '{}');
    settings[key] = value;
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
  }
};
