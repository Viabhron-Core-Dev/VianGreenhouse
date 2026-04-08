import React from 'react';
import { motion } from 'motion/react';
import * as Icons from 'lucide-react';
import { ASSISTANTS } from '../constants';
import { AssistantId } from '../types';
import { cn } from '../lib/utils';

interface SidebarProps {
  activeId: AssistantId;
  onSelect: (id: AssistantId) => void;
}

export function Sidebar({ activeId, onSelect }: SidebarProps) {
  return (
    <div className="w-20 md:w-64 h-full bg-card border-r flex flex-col transition-all duration-300">
      <div className="p-4 flex items-center gap-3">
        <div className="w-10 h-10 rounded-xl bg-primary flex items-center justify-center text-primary-foreground">
          <Icons.LayoutDashboard size={24} />
        </div>
        <span className="font-bold text-xl hidden md:block">AI Hub</span>
      </div>

      <nav className="flex-1 px-2 py-4 space-y-1">
        {ASSISTANTS.map((assistant) => {
          const Icon = (Icons as any)[assistant.icon] || Icons.HelpCircle;
          const isActive = activeId === assistant.id;

          return (
            <button
              key={assistant.id}
              onClick={() => onSelect(assistant.id)}
              className={cn(
                "w-full flex items-center gap-3 px-3 py-3 rounded-xl transition-all group relative",
                isActive 
                  ? "bg-primary/10 text-primary" 
                  : "hover:bg-muted text-muted-foreground hover:text-foreground"
              )}
            >
              <div 
                className={cn(
                  "w-10 h-10 rounded-lg flex items-center justify-center transition-transform group-hover:scale-110",
                  isActive ? "bg-primary text-primary-foreground" : "bg-muted"
                )}
                style={{ backgroundColor: isActive ? assistant.color : undefined }}
              >
                <Icon size={20} />
              </div>
              <div className="flex-1 text-left hidden md:block">
                <p className="font-medium text-sm leading-none">{assistant.name}</p>
                <p className="text-xs opacity-70 mt-1 truncate">{assistant.description}</p>
              </div>
              
              {isActive && (
                <motion.div 
                  layoutId="active-indicator"
                  className="absolute left-0 w-1 h-6 bg-primary rounded-r-full"
                />
              )}
            </button>
          );
        })}
      </nav>

      <div className="p-4 border-t space-y-1">
        <button className="w-full flex items-center gap-3 px-3 py-3 rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-all">
          <Icons.Settings size={20} />
          <span className="font-medium text-sm hidden md:block">Settings</span>
        </button>
      </div>
    </div>
  );
}
