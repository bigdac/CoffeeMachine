package vendingmachine.xr.com.coffeemachine.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class goods {
    @Id (autoincrement = false)
    private long goodsId;
    private float price;
    private int number;
    private String formulation ;
    private String efficiency ;
    private String listPic ;
    private String detailPic;
    private String goodsName;
    private int position;
    private int val;
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public long getGoodsId() {
        return this.goodsId;
    }
    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
    public int getVal() {
        return this.val;
    }
    public void setVal(int val) {
        this.val = val;
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
    @Generated(hash = 504024564)
    public goods(long goodsId, float price, int number, String formulation,
            String efficiency, String listPic, String detailPic, String goodsName,
            int position, int val) {
        this.goodsId = goodsId;
        this.price = price;
        this.number = number;
        this.formulation = formulation;
        this.efficiency = efficiency;
        this.listPic = listPic;
        this.detailPic = detailPic;
        this.goodsName = goodsName;
        this.position = position;
        this.val = val;
    }
    @Generated(hash = 761860576)
    public goods() {
    }
    

}
