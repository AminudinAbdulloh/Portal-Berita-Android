package com.example.portalberita;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PortalBerita.db";
    private static final int DATABASE_VERSION = 1;

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FULL_NAME = "full_name";

    // Table News
    private static final String TABLE_NEWS = "news";
    private static final String COL_NEWS_ID = "id";
    private static final String COL_NEWS_TITLE = "title";
    private static final String COL_NEWS_DESC = "description";
    private static final String COL_NEWS_CONTENT = "content";
    private static final String COL_NEWS_DATE = "date";
    private static final String COL_NEWS_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_FULL_NAME + " TEXT)";
        db.execSQL(createUsersTable);

        // Create News table
        String createNewsTable = "CREATE TABLE " + TABLE_NEWS + " (" +
                COL_NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NEWS_TITLE + " TEXT, " +
                COL_NEWS_DESC + " TEXT, " +
                COL_NEWS_CONTENT + " TEXT, " +
                COL_NEWS_DATE + " TEXT, " +
                COL_NEWS_IMAGE + " TEXT)";
        db.execSQL(createNewsTable);

        // Insert sample news data
        insertSampleNews(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(db);
    }

    // User Methods
    public boolean registerUser(String username, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_FULL_NAME, fullName);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public String getFullName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_FULL_NAME},
                COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(0);
            cursor.close();
            return fullName;
        }
        cursor.close();
        return "";
    }

    // News Methods
    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NEWS, null, null, null, null, null, COL_NEWS_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_NEWS_ID)));
                news.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_TITLE)));
                news.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_DESC)));
                news.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_CONTENT)));
                news.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_DATE)));
                news.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_IMAGE)));
                newsList.add(news);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return newsList;
    }

    public News getNewsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NEWS, null, COL_NEWS_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        News news = null;
        if (cursor.moveToFirst()) {
            news = new News();
            news.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_NEWS_ID)));
            news.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_TITLE)));
            news.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_DESC)));
            news.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_CONTENT)));
            news.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_DATE)));
            news.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWS_IMAGE)));
        }
        cursor.close();
        return news;
    }

    private void insertSampleNews(SQLiteDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
        String currentDate = sdf.format(new Date());

        // Array: [title, description, content, imageName]
        String[][] sampleNews = {
                {"SEA Games 2025: Kickboxing Sumbang Medali Emas ke-61 Indonesia",
                        "Atlet kickboxing Indonesia Riyan Jefri H Lumbanbatu sukses menyumbang medali emas ke-61 bagi kontingen Merah Putih di ajang SEA Games 2025",
                        "Riyan Jefri H Lumbanbatu berhasil meraih medali emas setelah tampil gemilang di babak final nomor K1-60kg putra. Bertanding di John Paul II Sports Center, Assumption University, pada Selasa (16/12/2025), Riyan sukses menundukkan perlawanan sengit dari atlet tuan rumah Thailand, Jakkrit Kongtook.\n\nKemenangan ini menjadi sangat spesial karena menggenapkan perolehan medali emas Indonesia menjadi 61 keping. Tambahan emas dari cabang olahraga kickboxing ini semakin memperkokoh posisi Indonesia di papan atas klasemen perolehan medali SEA Games 2025 yang berlangsung di Thailand.\n\nIndonesia sendiri mengirimkan kekuatan besar sebanyak 1.021 atlet yang berlaga di 49 dari 51 cabang olahraga yang dipertandingkan. Kontingen Indonesia ditargetkan untuk setidaknya mempertahankan peringkat tiga besar dengan estimasi raihan minimal 80 medali emas pada pesta olahraga Asia Tenggara edisi kali ini.",
                        "sea_games"},
                {"Pemerintah Mau Buka Sawah hingga 100 Ribu Ha di Papua",
                        "Pemerintah pusat berencana membuka lahan sawah seluas 100 ribu hektare di wilayah Papua untuk memenuhi kekurangan pasokan beras sebesar 500 ribu ton",
                        "Menteri Pertanian Andi Amran Sulaiman mengungkapkan bahwa Papua saat ini membutuhkan 660 ribu ton beras, namun produksinya baru mencapai 120 ribu ton. Untuk menutupi kekurangan tersebut, pemerintah akan membuka sawah baru yang tersebar di Provinsi Papua, Papua Selatan, dan Papua Barat dengan target penyelesaian dalam 2 hingga 3 tahun ke depan.\n\nPresiden Prabowo Subianto juga memberikan arahan khusus kepada kepala daerah di Papua untuk mengebut swasembada pangan dengan membangun lumbung pangan dari tingkat provinsi hingga desa. Pemerintah daerah diminta proaktif membuka lahan untuk sawah, jagung, sagu, dan singkong agar Papua dapat mandiri pangan tanpa bergantung pada pasokan dari pulau lain, yang juga diharapkan menjadi solusi permanen untuk masalah inflasi.",
                        "ricefields"},
                {"Data BPS: IKN Dihuni 147.427 Jiwa, Mayoritas Gen Z",
                        "Badan Pusat Statistik (BPS) merilis hasil Pendataan Penduduk Ibu Kota Nusantara (PPIKN) 2025 yang mencatat jumlah penduduk IKN mencapai 147.427 jiwa dengan didominasi oleh Generasi Z dan Milenial",
                        "Kepala BPS, Amalia Adininggar Widyasanti, memaparkan bahwa dari total populasi 147.427 jiwa (43.293 rumah tangga) di wilayah deliniasi IKN, Generasi Z mendominasi sebesar 27,20%, diikuti Milenial 23,53%. Dengan demikian, lebih dari setengah penduduk IKN, atau sekitar 50,73%, berada pada rentang usia muda (13-44 tahun). Selain itu, 67,91% penduduk IKN tercatat dalam kategori usia produktif.\n\nKepala Otorita IKN, Basuki Hadimuljono, menyatakan bahwa data demografi ini akan digunakan sebagai acuan perencanaan pembangunan kota dan penyediaan fasilitas publik. Mengingat dominasi penduduk muda, arah pembangunan akan disesuaikan dengan kebutuhan mereka, seperti menarik investor untuk pusat hiburan dan olahraga. Data ini juga mengungkap kondisi perumahan warga, di mana masih ditemukan rumah tangga tanpa fasilitas sanitasi memadai dan akses air bersih, yang akan menjadi fokus intervensi pemerintah ke depan.",
                        "ikn"},
                {"Guadalajara Vs Barcelona: El Barca Tak Frustrasi Meski Sempat Buntu",
                        "Pelatih Barcelona Hansi Flick memuji mentalitas dan kerja keras timnya yang berhasil menang 2-0 atas Guadalajara di ajang Copa del Rey meski sempat kesulitan menembus pertahanan lawan",
                        "Dalam pertandingan babak 32 besar Copa del Rey yang digelar di Estadio Pedro Escartin pada Rabu (17/12/2025), Barcelona tampil dominan dengan penguasaan bola mencapai 78 persen dan melepaskan 22 tembakan. Namun, El Barca sempat mengalami kebuntuan menghadapi pertahanan solid tuan rumah yang merupakan tim kasta ketiga Liga Spanyol, sebelum akhirnya Andreas Christensen memecah kebuntuan pada menit ke-75 dan Marcus Rashford menambah gol di menit akhir.\n\nHansi Flick menyatakan kepuasannya terhadap sikap para pemain yang tidak menunjukkan rasa frustrasi meski menghadapi situasi sulit. Menurutnya, kunci kemenangan terletak pada mentalitas yang baik dan kesabaran untuk terus menekan hingga akhirnya berhasil menembus pertahanan lawan dan memastikan tiket ke babak 16 besar.",
                        "barcelona"},
                {"Daftar Juara Dunia Formula 1, Terbaru Lando Norris",
                        "Lando Norris memastikan diri menjadi juara dunia Formula 1 musim 2025 setelah finis ketiga di GP Abu Dhabi, mengungguli Max Verstappen dengan selisih dua poin",
                        "Pebalap McLaren, Lando Norris, sukses meraih gelar juara dunia Formula 1 pertamanya setelah mengakhiri musim 2025 dengan total 423 poin. Kepastian ini didapat usai balapan terakhir di Yas Marina Circuit, Abu Dhabi, pada Minggu (7/12/2025), di mana Norris finis ketiga. Ia berhasil memutus dominasi Max Verstappen yang harus puas sebagai runner-up dengan selisih hanya dua poin.\n\nNorris menjadi pebalap Britania Raya ke-11 yang meraih gelar juara dunia dan pebalap McLaren kedelapan yang sukses menjadi kampiun, mengikuti jejak legenda seperti Ayrton Senna, Alain Prost, dan Lewis Hamilton. Kemenangan ini juga menandai pencapaian besar bagi McLaren yang terakhir kali memiliki juara dunia pada tahun 2008 lewat Lewis Hamilton.",
                        "formula_1"},
                {"Alex Marquez Merasa Kurang Dihargai Ducati",
                        "Alex Marquez merasa pencapaiannya sebagai runner-up MotoGP 2025 kurang dihargai oleh Ducati karena sering dibanding-bandingkan dengan kakaknya, Marc Marquez",
                        "Alex Marquez menutup musim MotoGP 2025 dengan pencapaian gemilang, finis sebagai runner-up dengan 467 poin dan meraih enam kemenangan, termasuk tiga kemenangan Grand Prix di Jerez, Catalunya, dan Malaysia. Meski demikian, pebalap Gresini berusia 29 tahun ini merasa diremehkan karena kesuksesannya sering dianggap semata-mata karena performa motor Ducati atau selalu dibandingkan dengan Marc Marquez, yang menjadi juara dunia musim ini.\n\nDalam wawancara, Alex mengungkapkan bahwa ia sering mendengar komentar yang mengecilkan usahanya, namun ia menyadari bahwa perbandingan dengan Marc—yang dianggap salah satu pebalap terbaik dalam sejarah—adalah hal yang wajar. Meski begitu, Alex menegaskan bahwa orang-orang di paddock yang benar-benar mengerti balapan telah melihat kemampuan sejatinya.",
                        "moto_gp"}
        };

        for (String[] news : sampleNews) {
            ContentValues values = new ContentValues();
            values.put(COL_NEWS_TITLE, news[0]);
            values.put(COL_NEWS_DESC, news[1]);
            values.put(COL_NEWS_CONTENT, news[2]);
            values.put(COL_NEWS_DATE, currentDate);
            values.put(COL_NEWS_IMAGE, news[3]); // Nama file gambar tanpa ekstensi
            db.insert(TABLE_NEWS, null, values);
        }
    }
}