import React, { useState } from 'react';
import { Sidebar } from './components/Sidebar';
import { GeminiChat } from './components/GeminiChat';
import { BrowserView } from './components/BrowserView';
import { ASSISTANTS } from './constants';
import { AssistantId } from './types';
import { ExternalLink, Info, Globe } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

export default function App() {
  const [activeId, setActiveId] = useState<AssistantId>('gemini');

  const activeAssistant = ASSISTANTS.find(a => a.id === activeId)!;

  const renderContent = () => {
    if (activeAssistant.isBuiltIn) {
      return (
        <motion.div
          key="built-in"
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          exit={{ opacity: 0, x: -20 }}
          className="flex-1 h-full"
        >
          <GeminiChat />
        </motion.div>
      );
    }

    if (activeAssistant.tabType === 'GIT' || activeAssistant.tabType === 'RESEARCH') {
      return (
        <motion.div
          key={activeId}
          initial={{ opacity: 0, scale: 0.98 }}
          animate={{ opacity: 1, scale: 1 }}
          exit={{ opacity: 0, scale: 1.02 }}
          className="flex-1 h-full"
        >
          <BrowserView assistant={activeAssistant} />
        </motion.div>
      );
    }

    return (
      <motion.div
        key={activeId}
        initial={{ opacity: 0, scale: 0.98 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 1.02 }}
        className="flex-1 flex flex-col items-center justify-center p-8 text-center"
      >
        <div 
          className="w-24 h-24 rounded-3xl flex items-center justify-center text-white mb-6 shadow-2xl shadow-primary/20"
          style={{ backgroundColor: activeAssistant.color }}
        >
          <Globe size={48} />
        </div>
        
        <h1 className="text-3xl font-bold mb-2">{activeAssistant.name}</h1>
        <p className="text-muted-foreground max-w-md mb-8">
          {activeAssistant.description}
        </p>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full max-w-lg">
          <a 
            href={activeAssistant.url} 
            target="_blank" 
            rel="noopener noreferrer"
            className="flex items-center justify-center gap-2 bg-primary text-primary-foreground px-6 py-4 rounded-2xl font-semibold hover:scale-105 transition-transform shadow-lg shadow-primary/20"
          >
            Open {activeAssistant.name}
            <ExternalLink size={18} />
          </a>
          
          <div className="flex items-center justify-center gap-2 bg-muted text-muted-foreground px-6 py-4 rounded-2xl font-medium">
            <Info size={18} />
            External Web App
          </div>
        </div>
        
        <div className="mt-12 p-6 bg-card border rounded-3xl max-w-xl text-left">
          <h3 className="font-semibold mb-2 flex items-center gap-2">
            <Info size={16} className="text-primary" />
            Why am I seeing this?
          </h3>
          <p className="text-sm text-muted-foreground leading-relaxed">
            Most AI assistants like {activeAssistant.name} prevent their websites from being embedded in other apps (using X-Frame-Options). 
            To provide the best experience, we recommend opening them in a new tab. 
            Your sessions will remain separate and secure.
          </p>
        </div>
      </motion.div>
    );
  };

  return (
    <div className="flex h-screen w-full bg-background overflow-hidden">
      <Sidebar activeId={activeId} onSelect={setActiveId} />
      
      <main className="flex-1 flex flex-col min-w-0">
        <AnimatePresence mode="wait">
          {renderContent()}
        </AnimatePresence>
      </main>
    </div>
  );
}

