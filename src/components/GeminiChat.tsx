import React, { useState, useRef, useEffect } from 'react';
import { GoogleGenAI } from '@google/genai';
import { motion } from 'motion/react';
import { Send, User, Bot, Sparkles, Loader2, Trash2 } from 'lucide-react';
import { Message } from '../types';

const ai = new GoogleGenAI({ apiKey: process.env.GEMINI_API_KEY || '' });

export function GeminiChat() {
  const [messages, setMessages] = useState<Message[]>(() => {
    const saved = localStorage.getItem('gemini_messages');
    return saved ? JSON.parse(saved) : [];
  });
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    localStorage.setItem('gemini_messages', JSON.stringify(messages));
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = async () => {
    if (!input.trim() || isLoading) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      role: 'user',
      content: input.trim(),
      timestamp: Date.now(),
    };

    setMessages(prev => [...prev, userMessage]);
    setInput('');
    setIsLoading(true);

    try {
      const response = await ai.models.generateContent({
        model: 'gemini-3-flash-preview',
        contents: [
          ...messages.map(m => ({
            role: m.role === 'user' ? 'user' : 'model',
            parts: [{ text: m.content }],
          })),
          { role: 'user', parts: [{ text: userMessage.content }] }
        ],
      });

      const text = response.text || 'No response received.';

      const botMessage: Message = {
        id: (Date.now() + 1).toString(),
        role: 'assistant',
        content: text,
        timestamp: Date.now(),
      };

      setMessages(prev => [...prev, botMessage]);
    } catch (error) {
      console.error('Gemini Error:', error);
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        role: 'assistant',
        content: 'Sorry, I encountered an error. Please check your API key and try again.',
        timestamp: Date.now(),
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const clearChat = () => {
    if (confirm('Are you sure you want to clear the chat history?')) {
      setMessages([]);
      localStorage.removeItem('gemini_messages');
    }
  };

  return (
    <div className="flex flex-col h-full bg-background relative">
      {/* Header */}
      <div className="p-4 border-b flex items-center justify-between bg-card/50 backdrop-blur-sm sticky top-0 z-10">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-lg bg-primary/10 flex items-center justify-center text-primary">
            <Sparkles size={18} />
          </div>
          <div>
            <h2 className="font-semibold text-sm">Gemini 1.5 Flash</h2>
            <p className="text-[10px] text-muted-foreground uppercase tracking-wider font-bold">Built-in Assistant</p>
          </div>
        </div>
        <button 
          onClick={clearChat}
          className="p-2 hover:bg-destructive/10 text-muted-foreground hover:text-destructive rounded-lg transition-colors"
          title="Clear chat"
        >
          <Trash2 size={18} />
        </button>
      </div>

      {/* Messages */}
      <div 
        ref={scrollRef}
        className="flex-1 overflow-y-auto p-4 space-y-6 scroll-smooth"
      >
        {messages.length === 0 ? (
          <div className="h-full flex flex-col items-center justify-center text-center p-8 space-y-4 opacity-50">
            <div className="w-16 h-16 rounded-2xl bg-muted flex items-center justify-center">
              <Sparkles size={32} />
            </div>
            <div>
              <p className="font-medium text-lg">Welcome to Gemini</p>
              <p className="text-sm max-w-xs">Ask me anything! I can help with coding, writing, analysis, and more.</p>
            </div>
          </div>
        ) : (
          messages.map((m) => (
            <motion.div
              key={m.id}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              className={`flex gap-4 ${m.role === 'user' ? 'flex-row-reverse' : ''}`}
            >
              <div className={`w-8 h-8 rounded-lg flex-shrink-0 flex items-center justify-center ${
                m.role === 'user' ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground'
              }`}>
                {m.role === 'user' ? <User size={16} /> : <Bot size={16} />}
              </div>
              <div className={`max-w-[80%] space-y-1 ${m.role === 'user' ? 'text-right' : ''}`}>
                <div className={`inline-block p-3 rounded-2xl text-sm leading-relaxed ${
                  m.role === 'user' 
                    ? 'bg-primary text-primary-foreground rounded-tr-none' 
                    : 'bg-card border rounded-tl-none'
                }`}>
                  {m.content}
                </div>
                <p className="text-[10px] text-muted-foreground">
                  {new Date(m.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
              </div>
            </motion.div>
          ))
        )}
        {isLoading && (
          <div className="flex gap-4">
            <div className="w-8 h-8 rounded-lg bg-muted flex items-center justify-center text-muted-foreground">
              <Bot size={16} />
            </div>
            <div className="bg-card border p-3 rounded-2xl rounded-tl-none">
              <Loader2 size={16} className="animate-spin text-primary" />
            </div>
          </div>
        )}
      </div>

      {/* Input */}
      <div className="p-4 border-t bg-card/50 backdrop-blur-sm">
        <div className="max-w-4xl mx-auto relative">
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                handleSend();
              }
            }}
            placeholder="Type your message..."
            className="w-full bg-background border rounded-2xl px-4 py-3 pr-12 text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all resize-none min-h-[50px] max-h-[200px]"
            rows={1}
          />
          <button
            onClick={handleSend}
            disabled={!input.trim() || isLoading}
            className="absolute right-2 bottom-2 p-2 bg-primary text-primary-foreground rounded-xl disabled:opacity-50 disabled:cursor-not-allowed hover:scale-105 active:scale-95 transition-all"
          >
            <Send size={18} />
          </button>
        </div>
        <p className="text-[10px] text-center text-muted-foreground mt-2">
          Gemini may display inaccurate info, so double-check its responses.
        </p>
      </div>
    </div>
  );
}
