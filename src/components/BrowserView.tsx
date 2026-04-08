import React, { useState, useEffect } from 'react';
import { Globe, ArrowLeft, ArrowRight, RotateCw, ExternalLink } from 'lucide-react';
import { Assistant } from '../types';

interface BrowserViewProps {
  assistant: Assistant;
}

export function BrowserView({ assistant }: BrowserViewProps) {
  const [url, setUrl] = useState(assistant.url);
  const [inputUrl, setInputUrl] = useState(assistant.url);
  const isResearch = assistant.tabType === 'RESEARCH';

  useEffect(() => {
    setUrl(assistant.url);
    setInputUrl(assistant.url);
  }, [assistant.url]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    let targetUrl = inputUrl;
    if (!targetUrl.startsWith('http')) {
      targetUrl = `https://duckduckgo.com/?q=${encodeURIComponent(targetUrl)}`;
    }
    setUrl(targetUrl);
    setInputUrl(targetUrl);
  };

  return (
    <div className="flex-1 flex flex-col h-full bg-background">
      {/* Browser Toolbar */}
      <div className="flex items-center gap-2 p-2 border-b bg-card/50 backdrop-blur-sm">
        <div className="flex items-center gap-1 px-2">
          <button className="p-1.5 hover:bg-muted rounded-lg text-muted-foreground disabled:opacity-30" disabled>
            <ArrowLeft size={16} />
          </button>
          <button className="p-1.5 hover:bg-muted rounded-lg text-muted-foreground disabled:opacity-30" disabled>
            <ArrowRight size={16} />
          </button>
          <button className="p-1.5 hover:bg-muted rounded-lg text-muted-foreground" onClick={() => setUrl(url)}>
            <RotateCw size={16} />
          </button>
        </div>

        <form onSubmit={handleSearch} className="flex-1 max-w-2xl mx-auto">
          <div className="relative group">
            <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none text-muted-foreground">
              <Globe size={14} />
            </div>
            <input
              type="text"
              value={inputUrl}
              onChange={(e) => setInputUrl(e.target.value)}
              disabled={!isResearch}
              className="w-full bg-muted/50 border rounded-full py-1.5 pl-9 pr-4 text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 focus:bg-background transition-all"
              placeholder={isResearch ? "Search or enter URL..." : assistant.url}
            />
          </div>
        </form>

        <div className="px-4">
          <a 
            href={url} 
            target="_blank" 
            rel="noopener noreferrer"
            className="flex items-center gap-2 text-xs font-medium text-primary hover:underline"
          >
            Open External
            <ExternalLink size={12} />
          </a>
        </div>
      </div>

      {/* Content Area */}
      <div className="flex-1 relative flex flex-col items-center justify-center p-8 text-center overflow-hidden">
        <div className="absolute inset-0 bg-grid-white/[0.02] -z-10" />
        
        <div 
          className="w-20 h-20 rounded-2xl flex items-center justify-center text-white mb-6 shadow-xl"
          style={{ backgroundColor: assistant.color }}
        >
          <Globe size={40} />
        </div>
        
        <h2 className="text-2xl font-bold mb-2">{assistant.name}</h2>
        <p className="text-muted-foreground max-w-md mb-8">
          {isResearch 
            ? "Use the search bar above to research documentation and solutions." 
            : `Access your ${assistant.name} repositories and workflows.`}
        </p>

        <div className="p-6 bg-card border rounded-3xl max-w-xl text-left shadow-sm">
          <p className="text-sm text-muted-foreground leading-relaxed">
            Due to security restrictions (X-Frame-Options), {assistant.name} cannot be embedded directly. 
            However, the VGH Fork provides this dedicated workspace to keep your {assistant.tabType === 'GIT' ? 'code' : 'research'} organized.
          </p>
          <div className="mt-6 flex justify-center">
            <a 
              href={url} 
              target="_blank" 
              rel="noopener noreferrer"
              className="inline-flex items-center gap-2 bg-primary text-primary-foreground px-8 py-3 rounded-xl font-semibold hover:scale-105 transition-transform shadow-lg shadow-primary/20"
            >
              Launch {assistant.name}
              <ExternalLink size={18} />
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
