package dLib.patchnotes;

import dLib.util.bindings.string.AbstractStringBinding;

import java.util.ArrayList;

public class Patchnotes {
    public ArrayList<PatchnotesEntry> entries = new ArrayList<>();

    public static class PatchnotesEntry {
        public AbstractStringBinding header;
        public AbstractStringBinding description;

        public PatchnotesEntry(AbstractStringBinding header, AbstractStringBinding description) {
            this.header = header;
            this.description = description;
        }
    }
}
