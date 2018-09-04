package com.xr.database.dao.daoImp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xr.database.dao.ADDao;
import com.xr.database.dao.DBManager;
import com.xr.database.dao.DaoMaster;
import com.xr.database.dao.DaoSession;
import com.xr.database.dao.goodsDao;

import java.util.List;

import vendingmachine.xr.com.coffeemachine.pojo.AD;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

public class ADDaoImp {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private ADDao adDao;
    private DaoSession session;
    public ADDaoImp(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        adDao = session.getADDao();
    }

    /**
     * 添加设备
     * @param ad
     */
    public void insert(AD ad){
        adDao.insert(ad);
    }

    /**
     * 删除设备
     * @param ad
     */
    public void delete(AD ad){
        adDao.delete(ad);
    }

    /**
     * 更新设备
     * @param ad
     */
    public void update(AD ad){
        adDao.update(ad);
    }

    public AD findById(long Id){
        return adDao.load(Id);
    }
    public List<AD> findAllad(){
        return adDao.loadAll();
    }
    public void  deleteAll(){
        adDao.deleteAll();
    }
}
