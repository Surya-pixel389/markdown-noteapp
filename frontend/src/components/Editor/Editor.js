import React, { useEffect } from 'react';
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import { Markdown } from 'tiptap-markdown';

export const Editor = ({ content, onChange }) => {
    const editor = useEditor({
        extensions: [
            StarterKit,
            Markdown.configure({
                html: false,
                transformPastedText: true,
            }),
        ],
        content: content,
        onUpdate: ({ editor }) => {
            const markdown = editor.storage.markdown.getMarkdown();
            onChange(markdown);
        },
    });

    useEffect(() => {
        if (editor && content !== editor.storage.markdown.getMarkdown()) {
            editor.commands.setContent(content);
        }
    }, [content]);

    return <EditorContent editor={editor} className="tiptap-editor" />;
};