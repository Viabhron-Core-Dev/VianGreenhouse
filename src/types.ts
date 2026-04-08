export type TabType = 'AI' | 'GIT' | 'RESEARCH';

export type AssistantId = 'gemini' | 'chatgpt' | 'claude' | 'perplexity' | 'deepseek' | 'grok' | 'github' | 'duckduckgo';

export interface Assistant {
  id: AssistantId;
  name: string;
  description: string;
  url: string;
  icon: string;
  color: string;
  isBuiltIn?: boolean;
  tabType?: TabType;
}

export interface VGHNote {
  id: string;
  title: string;
  content: string;
  createdAt: number;
  updatedAt: number;
}

export interface VGHQuickPrompt {
  id: string;
  name: string;
  content: string;
  sortOrder: number;
}

export interface VGHStagedFile {
  id: string;
  path: string;
  content: string;
  reason: string;
  timestamp: number;
}

export interface Message {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
}
