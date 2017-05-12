package com.twisty.lootlib;

/**
 * Project : Loot<br>
 * Created by twisty on 2017/5/12.<br>
 */

public class Album {
    private String albumName;
    private String latestImagePath;
    private int imageCount;

    public Album() {
    }

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getLatestImagePath() {
        return latestImagePath;
    }

    public void setLatestImagePath(String latestImagePath) {
        this.latestImagePath = latestImagePath;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumName='" + albumName + '\'' +
                ", latestImagePath='" + latestImagePath + '\'' +
                ", imageCount=" + imageCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;

        Album album = (Album) o;

        return getAlbumName().equals(album.getAlbumName());
    }

    @Override
    public int hashCode() {
        return getAlbumName().hashCode();
    }
}
