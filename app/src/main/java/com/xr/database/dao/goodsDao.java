package com.xr.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import vendingmachine.xr.com.coffeemachine.pojo.goods;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOODS".
*/
public class goodsDao extends AbstractDao<goods, Long> {

    public static final String TABLENAME = "GOODS";

    /**
     * Properties of entity goods.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property GoodsId = new Property(0, long.class, "goodsId", true, "_id");
        public final static Property Price = new Property(1, double.class, "price", false, "PRICE");
        public final static Property Number = new Property(2, int.class, "number", false, "NUMBER");
        public final static Property Formulation = new Property(3, String.class, "formulation", false, "FORMULATION");
        public final static Property Efficiency = new Property(4, String.class, "efficiency", false, "EFFICIENCY");
        public final static Property ListPic = new Property(5, String.class, "listPic", false, "LIST_PIC");
        public final static Property DetailPic = new Property(6, String.class, "detailPic", false, "DETAIL_PIC");
        public final static Property GoodsName = new Property(7, String.class, "goodsName", false, "GOODS_NAME");
        public final static Property Val = new Property(8, int.class, "val", false, "VAL");
        public final static Property Hd = new Property(9, String.class, "hd", false, "HD");
        public final static Property Hp = new Property(10, String.class, "hp", false, "HP");
        public final static Property HasGoods = new Property(11, int.class, "hasGoods", false, "HAS_GOODS");
        public final static Property WaterQuantity = new Property(12, int.class, "waterQuantity", false, "WATER_QUANTITY");
        public final static Property Temperature = new Property(13, int.class, "temperature", false, "TEMPERATURE");
    }


    public goodsDao(DaoConfig config) {
        super(config);
    }
    
    public goodsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOODS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: goodsId
                "\"PRICE\" REAL NOT NULL ," + // 1: price
                "\"NUMBER\" INTEGER NOT NULL ," + // 2: number
                "\"FORMULATION\" TEXT," + // 3: formulation
                "\"EFFICIENCY\" TEXT," + // 4: efficiency
                "\"LIST_PIC\" TEXT," + // 5: listPic
                "\"DETAIL_PIC\" TEXT," + // 6: detailPic
                "\"GOODS_NAME\" TEXT," + // 7: goodsName
                "\"VAL\" INTEGER NOT NULL ," + // 8: val
                "\"HD\" TEXT," + // 9: hd
                "\"HP\" TEXT," + // 10: hp
                "\"HAS_GOODS\" INTEGER NOT NULL ," + // 11: hasGoods
                "\"WATER_QUANTITY\" INTEGER NOT NULL ," + // 12: waterQuantity
                "\"TEMPERATURE\" INTEGER NOT NULL );"); // 13: temperature
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOODS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, goods entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getGoodsId());
        stmt.bindDouble(2, entity.getPrice());
        stmt.bindLong(3, entity.getNumber());
 
        String formulation = entity.getFormulation();
        if (formulation != null) {
            stmt.bindString(4, formulation);
        }
 
        String efficiency = entity.getEfficiency();
        if (efficiency != null) {
            stmt.bindString(5, efficiency);
        }
 
        String listPic = entity.getListPic();
        if (listPic != null) {
            stmt.bindString(6, listPic);
        }
 
        String detailPic = entity.getDetailPic();
        if (detailPic != null) {
            stmt.bindString(7, detailPic);
        }
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(8, goodsName);
        }
        stmt.bindLong(9, entity.getVal());
 
        String hd = entity.getHd();
        if (hd != null) {
            stmt.bindString(10, hd);
        }
 
        String hp = entity.getHp();
        if (hp != null) {
            stmt.bindString(11, hp);
        }
        stmt.bindLong(12, entity.getHasGoods());
        stmt.bindLong(13, entity.getWaterQuantity());
        stmt.bindLong(14, entity.getTemperature());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, goods entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getGoodsId());
        stmt.bindDouble(2, entity.getPrice());
        stmt.bindLong(3, entity.getNumber());
 
        String formulation = entity.getFormulation();
        if (formulation != null) {
            stmt.bindString(4, formulation);
        }
 
        String efficiency = entity.getEfficiency();
        if (efficiency != null) {
            stmt.bindString(5, efficiency);
        }
 
        String listPic = entity.getListPic();
        if (listPic != null) {
            stmt.bindString(6, listPic);
        }
 
        String detailPic = entity.getDetailPic();
        if (detailPic != null) {
            stmt.bindString(7, detailPic);
        }
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(8, goodsName);
        }
        stmt.bindLong(9, entity.getVal());
 
        String hd = entity.getHd();
        if (hd != null) {
            stmt.bindString(10, hd);
        }
 
        String hp = entity.getHp();
        if (hp != null) {
            stmt.bindString(11, hp);
        }
        stmt.bindLong(12, entity.getHasGoods());
        stmt.bindLong(13, entity.getWaterQuantity());
        stmt.bindLong(14, entity.getTemperature());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public goods readEntity(Cursor cursor, int offset) {
        goods entity = new goods( //
            cursor.getLong(offset + 0), // goodsId
            cursor.getDouble(offset + 1), // price
            cursor.getInt(offset + 2), // number
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // formulation
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // efficiency
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // listPic
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // detailPic
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // goodsName
            cursor.getInt(offset + 8), // val
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // hd
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // hp
            cursor.getInt(offset + 11), // hasGoods
            cursor.getInt(offset + 12), // waterQuantity
            cursor.getInt(offset + 13) // temperature
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, goods entity, int offset) {
        entity.setGoodsId(cursor.getLong(offset + 0));
        entity.setPrice(cursor.getDouble(offset + 1));
        entity.setNumber(cursor.getInt(offset + 2));
        entity.setFormulation(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEfficiency(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setListPic(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDetailPic(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGoodsName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setVal(cursor.getInt(offset + 8));
        entity.setHd(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setHp(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setHasGoods(cursor.getInt(offset + 11));
        entity.setWaterQuantity(cursor.getInt(offset + 12));
        entity.setTemperature(cursor.getInt(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(goods entity, long rowId) {
        entity.setGoodsId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(goods entity) {
        if(entity != null) {
            return entity.getGoodsId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(goods entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
