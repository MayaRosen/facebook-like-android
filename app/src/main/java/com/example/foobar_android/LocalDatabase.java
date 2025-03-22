package com.example.foobar_android;

        import androidx.room.Database;
        import androidx.room.Room;
        import androidx.room.RoomDatabase;

        import com.example.foobar_android.Daos.PostDao;
        import com.example.foobar_android.entities.Post;

@Database(entities = {Post.class}, version = 7, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
        public abstract PostDao postDao();

        private static volatile LocalDatabase INSTANCE;

        public static LocalDatabase getInstance() {
                if (INSTANCE == null) {
                        synchronized (LocalDatabase.class) {
                                if (INSTANCE == null) {
                                        // Create database here
                                        INSTANCE = Room.databaseBuilder(Foobar_Android.context.getApplicationContext(),
                                                        LocalDatabase.class, "db")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }
}
