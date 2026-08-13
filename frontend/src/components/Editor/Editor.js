import React from 'react';
import { Controlled as CodeMirror } from 'react-codemirror2';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/material-darker.css';
import 'codemirror/mode/markdown/markdown';

export const Editor = ({ content, onChange, onSave }) => {
  return (
    <div className="editor">
      <CodeMirror
        value={content}
        onBeforeChange={(editor, data, value) => onChange(value)}
        options={{
          mode: 'markdown',
          theme: 'material-darker',
          lineNumbers: true,
          lineWrapping: true,
          indentUnit: 2,
          tabSize: 2,
          indentWithTabs: false,
          autofocus: true,
        }}
        onKeyUp={(editor, event) => {
          if (event.ctrlKey && event.key === 's') {
            event.preventDefault();
            onSave();
          }
        }}
      />
    </div>
  );
};