package zzuli.com.music.pojo;

import java.io.Serializable;

public class Music implements Serializable {
    /**
     * 歌曲ID：MediaStore.Audio.Media._ID
     歌曲的名称：MediaStore.Audio.Media.TITLE
     歌曲的专辑名：MediaStore.Audio.Media.ALBUM
     歌曲的歌手名：MediaStore.Audio.Media.ARTIST
     歌曲文件的路径：MediaStore.Audio.Media.DATA
     歌曲的总播放时长：MediaStore.Audio.Media.DURATION
     歌曲文件的大小：MediaStore.Audio.Media.SIZE
     */

    //歌曲名
    private String title;
    //专辑
    private String album;
    //作者
    private String artist;
    //时长
    private long duration;

    private String albumId;
    private String url;


    public Music() {

    }
    /*  MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION

        */
    public Music(String title,String artist,String album,long duration,String albumId,String url) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.albumId = albumId;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", albumId=" + albumId +
                ", url='" + url + '\'' +
                '}';
    }
}

