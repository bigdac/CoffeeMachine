package vendingmachine.xr.com.coffeemachine.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class Order  implements Serializable{

    String orderNo;
    String aliCode;
    String wxCode;
    int result;
    public String getWxCode() {
        return this.wxCode;
    }
    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }
    public String getAliCode() {
        return this.aliCode;
    }
    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }
    public String getOrderNo() {
        return this.orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    @Generated(hash = 1774186203)
    public Order(String orderNo, String aliCode, String wxCode, int result) {
        this.orderNo = orderNo;
        this.aliCode = aliCode;
        this.wxCode = wxCode;
        this.result = result;
    }
    @Generated(hash = 1105174599)
    public Order() {
    }
  

}
