package com.xr.database.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.pojo.Order;
import vendingmachine.xr.com.coffeemachine.pojo.AD;

import com.xr.database.dao.goodsDao;
import com.xr.database.dao.OrderDao;
import com.xr.database.dao.ADDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig goodsDaoConfig;
    private final DaoConfig orderDaoConfig;
    private final DaoConfig aDDaoConfig;

    private final goodsDao goodsDao;
    private final OrderDao orderDao;
    private final ADDao aDDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        goodsDaoConfig = daoConfigMap.get(goodsDao.class).clone();
        goodsDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        aDDaoConfig = daoConfigMap.get(ADDao.class).clone();
        aDDaoConfig.initIdentityScope(type);

        goodsDao = new goodsDao(goodsDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);
        aDDao = new ADDao(aDDaoConfig, this);

        registerDao(goods.class, goodsDao);
        registerDao(Order.class, orderDao);
        registerDao(AD.class, aDDao);
    }
    
    public void clear() {
        goodsDaoConfig.clearIdentityScope();
        orderDaoConfig.clearIdentityScope();
        aDDaoConfig.clearIdentityScope();
    }

    public goodsDao getGoodsDao() {
        return goodsDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public ADDao getADDao() {
        return aDDao;
    }

}