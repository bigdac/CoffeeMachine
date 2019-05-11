package com.xr.database.dao.daoImp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.SaleGoodsDao;
import com.xr.database.dao.goodsDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

public class SaleGoodsDaoImp {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private SaleGoodsDao saleGoodsDao;
    private DaoSession session;
    public SaleGoodsDaoImp(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        saleGoodsDao = session.getSaleGoodsDao();
    }

    /**
     * 添加设备
     * @param goods
     */
    public void insert(SaleGoods goods){
        saleGoodsDao.insert(goods);
    }

    /**
     * 删除设备
     * @param goods
     */
    public void delete(SaleGoods goods){
        saleGoodsDao.delete(goods);
    }

    /**
     * 更新设备
     * @param goods
     */
    public void update(SaleGoods goods){
        saleGoodsDao.update(goods);
    }

    public SaleGoods findById(long goodsId){
        return saleGoodsDao.load(goodsId);
    }
    public List<SaleGoods> findAllgoods(){
        return saleGoodsDao.loadAll();
    }
    public List <SaleGoods> findByGoodsID (long goodsId){
        return saleGoodsDao.queryBuilder().where(SaleGoodsDao.Properties.GoodsId.eq(goodsId)).list();
    }
    public  SaleGoods findByHdHp(String hd,String hp){
        return saleGoodsDao.queryBuilder().where(SaleGoodsDao.Properties.Hd.eq(hd),SaleGoodsDao.Properties.Hp.eq(hp)).unique();
    }

    public void  deleteAll(){
        saleGoodsDao.deleteAll();
    }
}
