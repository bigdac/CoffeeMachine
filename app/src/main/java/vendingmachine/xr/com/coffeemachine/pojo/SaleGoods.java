package vendingmachine.xr.com.coffeemachine.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SaleGoods {
    @Id(autoincrement = false)
    private Long Id;
    private Long goodsId;
    private String hd;
    private String hp;
    private int hasGoods;
    private int goodsNum;
    private int waterQuantity;
    private int temperature;
    public int getTemperature() {
        return this.temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getWaterQuantity() {
        return this.waterQuantity;
    }
    public void setWaterQuantity(int waterQuantity) {
        this.waterQuantity = waterQuantity;
    }
    public int getGoodsNum() {
        return this.goodsNum;
    }
    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }
    public int getHasGoods() {
        return this.hasGoods;
    }
    public void setHasGoods(int hasGoods) {
        this.hasGoods = hasGoods;
    }
    public String getHp() {
        return this.hp;
    }
    public void setHp(String hp) {
        this.hp = hp;
    }
    public String getHd() {
        return this.hd;
    }
    public void setHd(String hd) {
        this.hd = hd;
    }
    public Long getGoodsId() {
        return this.goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    @Generated(hash = 652663398)
    public SaleGoods(Long Id, Long goodsId, String hd, String hp, int hasGoods,
            int goodsNum, int waterQuantity, int temperature) {
        this.Id = Id;
        this.goodsId = goodsId;
        this.hd = hd;
        this.hp = hp;
        this.hasGoods = hasGoods;
        this.goodsNum = goodsNum;
        this.waterQuantity = waterQuantity;
        this.temperature = temperature;
    }
    @Generated(hash = 1544804335)
    public SaleGoods() {
    }

    @Override
    public String toString() {
        return "SaleGoods{" +
                "Id=" + Id +
                ", goodsId=" + goodsId +
                ", hd='" + hd + '\'' +
                ", hp='" + hp + '\'' +
                ", hasGoods=" + hasGoods +
                ", goodsNum=" + goodsNum +
                ", waterQuantity=" + waterQuantity +
                ", temperature=" + temperature +
                '}';
    }
}
