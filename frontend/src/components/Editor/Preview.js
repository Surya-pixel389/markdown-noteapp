import React from 'react';
import ReactMarkdown from 'react-markdown';

export const Preview = ({ content }) => {
  return (
    <div className="preview">
      <div className="preview-content">
        <ReactMarkdown>{content}</ReactMarkdown>
      </div>
    </div>
  );
};