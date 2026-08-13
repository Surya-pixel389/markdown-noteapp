import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';
import { Moon, Sun, LogOut } from 'lucide-react';
import './Navbar.css';

export const Navbar = () => {
  const { user, logout } = useAuth();
  const { isDark, toggleTheme } = useTheme();

  const handleLogout = () => {
    logout();
    window.location.href = '/login';
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <h1>📝 NoteApp</h1>
      </div>
      <div className="navbar-right">
        <span className="user-name">{user?.fullName}</span>
        <button onClick={toggleTheme} className="theme-toggle" title="Toggle theme">
          {isDark ? <Sun size={20} /> : <Moon size={20} />}
        </button>
        <button onClick={handleLogout} className="logout-btn" title="Logout">
          <LogOut size={20} />
        </button>
      </div>
    </nav>
  );
};