package vendingmachine.xr.com.coffeemachine.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AD {
    @Id(autoincrement = false)
    Long id ;
    String adVideo;
    String adPictire;
    public String getAdPictire() {
        return this.adPictire;
    }
    public void setAdPictire(String adPictire) {
        this.adPictire = adPictire;
    }
    public String getAdVideo() {
        return this.adVideo;
    }
    public void setAdVideo(String adVideo) {
        this.adVideo = adVideo;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 304694046)
    public AD(Long id, String adVideo, String adPictire) {
        this.id = id;
        this.adVideo = adVideo;
        this.adPictire = adPictire;
    }
    @Generated(hash = 293030498)
    public AD() {
    }
    

}
