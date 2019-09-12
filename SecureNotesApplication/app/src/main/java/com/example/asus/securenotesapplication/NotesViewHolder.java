package com.example.asus.securenotesapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView textTitle, textContent;

    public NotesViewHolder(View itemView)
    {
        super(itemView);
        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textContent = mView.findViewById(R.id.note_content);
    }

    public void setNoteTitle(String title)
    {
        textTitle.setText(title);
    }

    public void setNoteContent(String content)
    {
        textContent.setText(content);
    }
}
