package tce.education.koodalnraghavan.classes;

public class AlwarMusicDetails {

    public String audioName;
    public String audioUrl;
    public String format;
    public String price;

    public AlwarMusicDetails(String audioName,String audioUrl,String format,String price) {
        this.audioName = audioName;
        this.audioUrl = audioUrl;
        this.format = format;
        this.price = price;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
