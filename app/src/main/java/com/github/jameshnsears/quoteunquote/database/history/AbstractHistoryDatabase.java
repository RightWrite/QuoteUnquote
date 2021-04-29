package com.github.jameshnsears.quoteunquote.database.history;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import timber.log.Timber;

@Database(
        entities = {PreviousEntity.class, FavouriteEntity.class, ReportedEntity.class, CurrentEntity.class},
        version = 2)
public abstract class AbstractHistoryDatabase extends RoomDatabase {
    @NonNull
    public static final String DATABASE_NAME = "history.db";
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
            Timber.d(AbstractHistoryDatabase.DATABASE_NAME);
            database.execSQL("CREATE TABLE IF NOT EXISTS `current` (`widget_id` INTEGER NOT NULL, `digest` TEXT NOT NULL, PRIMARY KEY(`widget_id`))");

            database.execSQL("DELETE FROM `previous`");

            database.execSQL("CREATE INDEX IF NOT EXISTS `index_previous_digest` ON `previous` (`digest`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_previous_widget_id_content_type_digest` ON `previous` (`widget_id`, `content_type`, `digest`)");
        }
    };
    @Nullable
    public static AbstractHistoryDatabase historyDatabase;

    @NonNull
    public static AbstractHistoryDatabase getDatabase(@NonNull Context context) {
        synchronized (AbstractHistoryDatabase.class) {
            Timber.d("%b", AbstractHistoryDatabase.historyDatabase == null);
            if (AbstractHistoryDatabase.historyDatabase == null) {
                AbstractHistoryDatabase.historyDatabase = Room.databaseBuilder(context,
                        AbstractHistoryDatabase.class, AbstractHistoryDatabase.DATABASE_NAME)
//                        .createFromAsset(DATABASE_NAME)
                        .addMigrations(AbstractHistoryDatabase.MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build();
            }

            return AbstractHistoryDatabase.historyDatabase;
        }
    }

    public abstract PreviousDAO previousDAO();

    public abstract FavouriteDAO favouritesDAO();

    public abstract ReportedDAO reportedDAO();

    public abstract CurrentDAO currentDAO();
}
