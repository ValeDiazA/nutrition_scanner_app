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
import com.nutritionscanner.domain.model.Achievement;
import com.nutritionscanner.domain.model.UserPreferences;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final DatabaseConverters __databaseConverters = new DatabaseConverters();

  private final EntityDeletionOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfAddPoints;

  private final SharedSQLiteStatement __preparedStmtOfIncrementScannedProducts;

  private final SharedSQLiteStatement __preparedStmtOfIncrementHealthyChoices;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`name`,`email`,`level`,`totalPoints`,`stellarPublicKey`,`stellarBalance`,`scannedProducts`,`healthyChoices`,`totalDonations`,`achievements`,`preferences`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getLevel());
        statement.bindLong(5, entity.getTotalPoints());
        if (entity.getStellarPublicKey() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStellarPublicKey());
        }
        statement.bindDouble(7, entity.getStellarBalance());
        statement.bindLong(8, entity.getScannedProducts());
        statement.bindLong(9, entity.getHealthyChoices());
        statement.bindDouble(10, entity.getTotalDonations());
        final String _tmp = __databaseConverters.fromAchievementList(entity.getAchievements());
        statement.bindString(11, _tmp);
        final String _tmp_1 = __databaseConverters.fromUserPreferences(entity.getPreferences());
        statement.bindString(12, _tmp_1);
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`name` = ?,`email` = ?,`level` = ?,`totalPoints` = ?,`stellarPublicKey` = ?,`stellarBalance` = ?,`scannedProducts` = ?,`healthyChoices` = ?,`totalDonations` = ?,`achievements` = ?,`preferences` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmail());
        statement.bindLong(4, entity.getLevel());
        statement.bindLong(5, entity.getTotalPoints());
        if (entity.getStellarPublicKey() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStellarPublicKey());
        }
        statement.bindDouble(7, entity.getStellarBalance());
        statement.bindLong(8, entity.getScannedProducts());
        statement.bindLong(9, entity.getHealthyChoices());
        statement.bindDouble(10, entity.getTotalDonations());
        final String _tmp = __databaseConverters.fromAchievementList(entity.getAchievements());
        statement.bindString(11, _tmp);
        final String _tmp_1 = __databaseConverters.fromUserPreferences(entity.getPreferences());
        statement.bindString(12, _tmp_1);
        statement.bindString(13, entity.getId());
      }
    };
    this.__preparedStmtOfAddPoints = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET totalPoints = totalPoints + ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementScannedProducts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET scannedProducts = scannedProducts + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementHealthyChoices = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET healthyChoices = healthyChoices + 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addPoints(final String userId, final int points,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddPoints.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, points);
        _argIndex = 2;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfAddPoints.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementScannedProducts(final String userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementScannedProducts.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfIncrementScannedProducts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementHealthyChoices(final String userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementHealthyChoices.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfIncrementHealthyChoices.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getUserById(final String id, final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfTotalPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPoints");
          final int _cursorIndexOfStellarPublicKey = CursorUtil.getColumnIndexOrThrow(_cursor, "stellarPublicKey");
          final int _cursorIndexOfStellarBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "stellarBalance");
          final int _cursorIndexOfScannedProducts = CursorUtil.getColumnIndexOrThrow(_cursor, "scannedProducts");
          final int _cursorIndexOfHealthyChoices = CursorUtil.getColumnIndexOrThrow(_cursor, "healthyChoices");
          final int _cursorIndexOfTotalDonations = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDonations");
          final int _cursorIndexOfAchievements = CursorUtil.getColumnIndexOrThrow(_cursor, "achievements");
          final int _cursorIndexOfPreferences = CursorUtil.getColumnIndexOrThrow(_cursor, "preferences");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpTotalPoints;
            _tmpTotalPoints = _cursor.getInt(_cursorIndexOfTotalPoints);
            final String _tmpStellarPublicKey;
            if (_cursor.isNull(_cursorIndexOfStellarPublicKey)) {
              _tmpStellarPublicKey = null;
            } else {
              _tmpStellarPublicKey = _cursor.getString(_cursorIndexOfStellarPublicKey);
            }
            final double _tmpStellarBalance;
            _tmpStellarBalance = _cursor.getDouble(_cursorIndexOfStellarBalance);
            final int _tmpScannedProducts;
            _tmpScannedProducts = _cursor.getInt(_cursorIndexOfScannedProducts);
            final int _tmpHealthyChoices;
            _tmpHealthyChoices = _cursor.getInt(_cursorIndexOfHealthyChoices);
            final double _tmpTotalDonations;
            _tmpTotalDonations = _cursor.getDouble(_cursorIndexOfTotalDonations);
            final List<Achievement> _tmpAchievements;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAchievements);
            _tmpAchievements = __databaseConverters.toAchievementList(_tmp);
            final UserPreferences _tmpPreferences;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfPreferences);
            _tmpPreferences = __databaseConverters.toUserPreferences(_tmp_1);
            _result = new UserEntity(_tmpId,_tmpName,_tmpEmail,_tmpLevel,_tmpTotalPoints,_tmpStellarPublicKey,_tmpStellarBalance,_tmpScannedProducts,_tmpHealthyChoices,_tmpTotalDonations,_tmpAchievements,_tmpPreferences);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
