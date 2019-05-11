package vendingmachine.xr.com.coffeemachine.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class goods {
    @Id (autoincrement = false)
    private long goodsId;
    private double price;
    private int number;
    private String formulation ;
    private String efficiency ;
    private String listPic ;
    private String detailPic;
    private String goodsName;
    private int val;
    private String hd;
    private String hp;
    private int hasGoods;
    private int waterQuantity;
    private int temperature;

    @Generated(hash = 1744225466)
    public goods(long goodsId, double price, int number, String formulation,
            String efficiency, String listPic, String detailPic, String goodsName,
            int val, String hd, String hp, int hasGoods, int waterQuantity,
            int temperature) {
        this.goodsId = goodsId;
        this.price = price;
        this.number = number;
        this.formulation = formulation;
        this.efficiency = efficiency;
        this.listPic = listPic;
        this.detailPic = detailPic;
        this.goodsName = goodsName;
        this.val = val;
        this.hd = hd;
        this.hp = hp;
        this.hasGoods = hasGoods;
        this.waterQuantity = waterQuantity;
        this.temperature = temperature;
    }

    @Generated(hash = 761860576)
    public goods() {
    }

    @Override
    public String toString() {
        return "goods{" +
                "goodsId=" + goodsId +
                ", hd='" + hd + '\'' +
                ", hp='" + hp + '\'' +
                ", waterQuantity=" + waterQuantity +
                ", temperature=" + temperature +
                '}';
    }

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

    public int getVal() {
        return this.val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDetailPic() {
        return this.detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic;
    }

    public String getListPic() {
        return this.listPic;
    }

    public void setListPic(String listPic) {
        this.listPic = listPic;
    }

    public String getEfficiency() {
        return this.efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getFormulation() {
        return this.formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
