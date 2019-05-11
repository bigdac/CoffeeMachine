package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SALE_GOODS".
*/
public class SaleGoodsDao extends AbstractDao<SaleGoods, Long> {

    public static final String TABLENAME = "SALE_GOODS";

    /**
     * Properties of entity SaleGoods.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property GoodsId = new Property(1, Long.class, "goodsId", false, "GOODS_ID");
        public final static Property Hd = new Property(2, String.class, "hd", false, "HD");
        public final static Property Hp = new Property(3, String.class, "hp", false, "HP");
        public final static Property HasGoods = new Property(4, int.class, "hasGoods", false, "HAS_GOODS");
        public final static Property GoodsNum = new Property(5, int.class, "goodsNum", false, "GOODS_NUM");
        public final static Property WaterQuantity = new Property(6, int.class, "waterQuantity", false, "WATER_QUANTITY");
        public final static Property Temperature = new Property(7, int.class, "temperature", false, "TEMPERATURE");
    }


    public SaleGoodsDao(DaoConfig config) {
        super(config);
    }
    
    public SaleGoodsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SALE_GOODS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: Id
                "\"GOODS_ID\" INTEGER," + // 1: goodsId
                "\"HD\" TEXT," + // 2: hd
                "\"HP\" TEXT," + // 3: hp
                "\"HAS_GOODS\" INTEGER NOT NULL ," + // 4: hasGoods
                "\"GOODS_NUM\" INTEGER NOT NULL ," + // 5: goodsNum
                "\"WATER_QUANTITY\" INTEGER NOT NULL ," + // 6: waterQuantity
                "\"TEMPERATURE\" INTEGER NOT NULL );"); // 7: temperature
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SALE_GOODS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SaleGoods entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        Long goodsId = entity.getGoodsId();
        if (goodsId != null) {
            stmt.bindLong(2, goodsId);
        }
 
        String hd = entity.getHd();
        if (hd != null) {
            stmt.bindString(3, hd);
        }
 
        String hp = entity.getHp();
        if (hp != null) {
            stmt.bindString(4, hp);
        }
        stmt.bindLong(5, entity.getHasGoods());
        stmt.bindLong(6, entity.getGoodsNum());
        stmt.bindLong(7, entity.getWaterQuantity());
        stmt.bindLong(8, entity.getTemperature());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SaleGoods entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        Long goodsId = entity.getGoodsId();
        if (goodsId != null) {
            stmt.bindLong(2, goodsId);
        }
 
        String hd = entity.getHd();
        if (hd != null) {
            stmt.bindString(3, hd);
        }
 
        String hp = entity.getHp();
        if (hp != null) {
            stmt.bindString(4, hp);
        }
        stmt.bindLong(5, entity.getHasGoods());
        stmt.bindLong(6, entity.getGoodsNum());
        stmt.bindLong(7, entity.getWaterQuantity());
        stmt.bindLong(8, entity.getTemperature());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SaleGoods readEntity(Cursor cursor, int offset) {
        SaleGoods entity = new SaleGoods( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // goodsId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // hd
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // hp
            cursor.getInt(offset + 4), // hasGoods
            cursor.getInt(offset + 5), // goodsNum
            cursor.getInt(offset + 6), // waterQuantity
            cursor.getInt(offset + 7) // temperature
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SaleGoods entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGoodsId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setHd(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHp(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHasGoods(cursor.getInt(offset + 4));
        entity.setGoodsNum(cursor.getInt(offset + 5));
        entity.setWaterQuantity(cursor.getInt(offset + 6));
        entity.setTemperature(cursor.getInt(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SaleGoods entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SaleGoods entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SaleGoods entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}