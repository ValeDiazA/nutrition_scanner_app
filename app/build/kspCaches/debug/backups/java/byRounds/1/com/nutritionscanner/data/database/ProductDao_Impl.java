package com.nutritionscanner.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nutritionscanner.domain.model.Ingredient;
import com.nutritionscanner.domain.model.LatamLabeling;
import com.nutritionscanner.domain.model.NutritionalInfo;
import com.nutritionscanner.domain.model.NutritionalScore;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProductEntity> __insertionAdapterOfProductEntity;

  private final DatabaseConverters __databaseConverters = new DatabaseConverters();

  private final EntityDeletionOrUpdateAdapter<ProductEntity> __deletionAdapterOfProductEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllProducts;

  public ProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProductEntity = new EntityInsertionAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`id`,`name`,`brand`,`barcode`,`ingredients`,`nutritionalInfo`,`score`,`latamLabeling`,`scanTimestamp`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBrand());
        if (entity.getBarcode() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBarcode());
        }
        final String _tmp = __databaseConverters.fromIngredientList(entity.getIngredients());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __databaseConverters.fromNutritionalInfo(entity.getNutritionalInfo());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        final String _tmp_2 = __databaseConverters.fromNutritionalScore(entity.getScore());
        statement.bindString(7, _tmp_2);
        final String _tmp_3 = __databaseConverters.fromLatamLabeling(entity.getLatamLabeling());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        statement.bindLong(9, entity.getScanTimestamp());
      }
    };
    this.__deletionAdapterOfProductEntity = new EntityDeletionOrUpdateAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `products` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllProducts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM products";
        return _query;
      }
    };
  }

  @Override
  public Object insertProduct(final ProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProduct(final ProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProductEntity.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllProducts(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllProducts.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllProducts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductEntity>> getAllProducts() {
    final String _sql = "SELECT * FROM products ORDER BY scanTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfNutritionalInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "nutritionalInfo");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfLatamLabeling = CursorUtil.getColumnIndexOrThrow(_cursor, "latamLabeling");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpBarcode;
            if (_cursor.isNull(_cursorIndexOfBarcode)) {
              _tmpBarcode = null;
            } else {
              _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            }
            final List<Ingredient> _tmpIngredients;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __databaseConverters.toIngredientList(_tmp);
            final NutritionalInfo _tmpNutritionalInfo;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfNutritionalInfo)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfNutritionalInfo);
            }
            _tmpNutritionalInfo = __databaseConverters.toNutritionalInfo(_tmp_1);
            final NutritionalScore _tmpScore;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfScore);
            _tmpScore = __databaseConverters.toNutritionalScore(_tmp_2);
            final LatamLabeling _tmpLatamLabeling;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLatamLabeling)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfLatamLabeling);
            }
            _tmpLatamLabeling = __databaseConverters.toLatamLabeling(_tmp_3);
            final long _tmpScanTimestamp;
            _tmpScanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _item = new ProductEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpIngredients,_tmpNutritionalInfo,_tmpScore,_tmpLatamLabeling,_tmpScanTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getProductById(final String id,
      final Continuation<? super ProductEntity> $completion) {
    final String _sql = "SELECT * FROM products WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProductEntity>() {
      @Override
      @Nullable
      public ProductEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfNutritionalInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "nutritionalInfo");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfLatamLabeling = CursorUtil.getColumnIndexOrThrow(_cursor, "latamLabeling");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final ProductEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpBarcode;
            if (_cursor.isNull(_cursorIndexOfBarcode)) {
              _tmpBarcode = null;
            } else {
              _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            }
            final List<Ingredient> _tmpIngredients;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __databaseConverters.toIngredientList(_tmp);
            final NutritionalInfo _tmpNutritionalInfo;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfNutritionalInfo)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfNutritionalInfo);
            }
            _tmpNutritionalInfo = __databaseConverters.toNutritionalInfo(_tmp_1);
            final NutritionalScore _tmpScore;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfScore);
            _tmpScore = __databaseConverters.toNutritionalScore(_tmp_2);
            final LatamLabeling _tmpLatamLabeling;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLatamLabeling)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfLatamLabeling);
            }
            _tmpLatamLabeling = __databaseConverters.toLatamLabeling(_tmp_3);
            final long _tmpScanTimestamp;
            _tmpScanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _result = new ProductEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpIngredients,_tmpNutritionalInfo,_tmpScore,_tmpLatamLabeling,_tmpScanTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductEntity>> getRecentProducts(final int limit) {
    final String _sql = "SELECT * FROM products ORDER BY scanTimestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfNutritionalInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "nutritionalInfo");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfLatamLabeling = CursorUtil.getColumnIndexOrThrow(_cursor, "latamLabeling");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpBarcode;
            if (_cursor.isNull(_cursorIndexOfBarcode)) {
              _tmpBarcode = null;
            } else {
              _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            }
            final List<Ingredient> _tmpIngredients;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __databaseConverters.toIngredientList(_tmp);
            final NutritionalInfo _tmpNutritionalInfo;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfNutritionalInfo)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfNutritionalInfo);
            }
            _tmpNutritionalInfo = __databaseConverters.toNutritionalInfo(_tmp_1);
            final NutritionalScore _tmpScore;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfScore);
            _tmpScore = __databaseConverters.toNutritionalScore(_tmp_2);
            final LatamLabeling _tmpLatamLabeling;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLatamLabeling)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfLatamLabeling);
            }
            _tmpLatamLabeling = __databaseConverters.toLatamLabeling(_tmp_3);
            final long _tmpScanTimestamp;
            _tmpScanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            _item = new ProductEntity(_tmpId,_tmpName,_tmpBrand,_tmpBarcode,_tmpIngredients,_tmpNutritionalInfo,_tmpScore,_tmpLatamLabeling,_tmpScanTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getHealthyProductsCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM products WHERE JSON_EXTRACT(score, '$.trafficLight') = 'GREEN'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
