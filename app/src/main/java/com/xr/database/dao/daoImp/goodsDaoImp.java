package com.xr.database.dao.daoImp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.goodsDao;
import java.util.List;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

public class goodsDaoImp {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private goodsDao goodsDao;
    private DaoSession session;
    public goodsDaoImp(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        goodsDao = session.getGoodsDao();
    }

    /**
     * 添加设备
     * @param goods
     */
    public void insert(goods goods){
        goodsDao.insert(goods);
    }

    /**
     * 删除设备
     * @param goods
     */
    public void delete(goods goods){
        goodsDao.delete(goods);
    }

    /**
     * 更新设备
     * @param goods
     */
    public void update(goods goods){
        goodsDao.update(goods);
    }

    public goods findById(long goodsId){
        return goodsDao.load(goodsId);
    }
    public List<goods> findAllgoods(){
        return goodsDao.loadAll();
    }
    public void  deleteAll(){
        goodsDao.deleteAll();
    }
}
