package me.alpha432.oyvey.util;

import org.lwjgl.opengl.*;

public class FontCache
{
    int displayList;
    long lastUsage;
    boolean deleted;

    public FontCache(final int displayList, final long lastUsage, final boolean deleted) {
        this.deleted = false;
        this.displayList = displayList;
        this.lastUsage = lastUsage;
        this.deleted = deleted;
    }

    public FontCache(final int displayList, final long lastUsage) {
        this.deleted = false;
        this.displayList = displayList;
        this.lastUsage = lastUsage;
    }

    @Override
    protected void finalize() {
        if (!this.deleted) {
            GL11.glDeleteLists(this.displayList, 1);
        }
    }

    public int getDisplayList() {
        return this.displayList;
    }

    public long getLastUsage() {
        return this.lastUsage;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setLastUsage(final long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}