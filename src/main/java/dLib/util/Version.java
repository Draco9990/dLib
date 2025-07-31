package dLib.util;

import com.vdurmont.semver4j.Semver;

import java.io.Serializable;

public class Version implements Serializable, Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;

    private final boolean alpha;
    private final boolean beta;

    public Version(int major, int minor, int patch) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers must be non-negative.");
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.alpha = false;
        this.beta = false;
    }

    public Version(int major, int minor, int patch, boolean alpha, boolean beta) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers must be non-negative.");
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.alpha = alpha;
        this.beta = beta;
    }

    public Version(String version){
        String[] parts = version.split("\\.");
        if(parts.length != 3){
            throw new IllegalArgumentException("Version string must have 3 parts.");
        }

        if(parts[0].startsWith("v")){
            parts[0] = parts[0].substring(1);
        }

        if(parts[2].endsWith("A") || parts[2].endsWith("B") || parts[2].endsWith("a") || parts[2].endsWith("b")){
            parts[2] = parts[2].substring(0, parts[2].length() - 1);
        }

        parts[0] = parts[0].replaceAll("[^0-9]", "");
        parts[1] = parts[1].replaceAll("[^0-9]", "");
        parts[2] = parts[2].replaceAll("[^0-9]", "");

        this.major = Integer.parseInt(parts[0]);
        this.minor = Integer.parseInt(parts[1]);
        this.patch = Integer.parseInt(parts[2]);

        this.alpha = version.endsWith("A");
        this.beta = version.endsWith("B");
    }

    public Version(Semver semver){
        this.major = semver.getMajor();
        this.minor = semver.getMinor();
        this.patch = semver.getPatch();

        this.alpha = false;
        this.beta = false;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(Version other) {
        if(this.alpha && !other.alpha){
            return -1;
        } else if(!this.alpha && other.alpha){
            return 1;
        }

        if(this.beta && !other.beta){
            return -1;
        } else if(!this.beta && other.beta){
            return 1;
        }

        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }

        return Integer.compare(this.patch, other.patch);
    }

    public boolean isOlderThan(Version other) {
        return this.compareTo(other) < 0;
    }
    public boolean isNewerThan(Version other) {
        return this.compareTo(other) > 0;
    }
    public boolean isSameAs(Version other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Version version = (Version) obj;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return 31 * major + 17 * minor + patch;
    }
}
