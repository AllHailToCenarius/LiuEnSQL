package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *  To:Jack Liu( i forget if your name is Jack,sorry.
 *      用法参见{@link MyClass.main()}中的例子。
 *      只实现了User/Photo/PhotoDetail/PhotoComment/PhotoCatrgory 的增删改查。
 *      使用的时候请先先设置数据库/用户名/密码，在postgreUtil的getConnection方法中。
 *      另外请保证表是存在的，可以参考http://blog.csdn.net/wangyezi19930928/article/details/44559685，将本目录下的exampledb.sql导入到postgresql.
 *
 *      有时间可以作如下优化：  getConnection与数据库连接池建立关系；
 *                          {@link PosgreUtil,parseTags}和{@link PosgreUtil.loadTags}里面的数组和字符串处理进行改进。
 *                          优化业务逻辑/细化模块，现在getPhoto会同时获取到Photo的所有Comment和Detail，可以考虑在需要的时候在加载。
 *                          安全提升：数据库中密码明文存放，可以改成单向hash（如MD5等算法)，只能进行校验对比，防止信息泄露。
 *
 */

public class PosgreUtil {

    private Connection mConnection = null;
    private Statement mStatement = null;

    private static final String TABLE_USERS = "users";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_PASSWORD = "password";

    private static final String TABLE_PHOTO = "photo";
    private static final String PHOTO_ID = "id";
    private static final String PHOTO_TITLE = "title";
    private static final String PHOTO_AUTHOR = "author";
    private static final String PHOTO_TAGS = "tags";
    private static final String PHOTO_CONTENT = "essay";

    private static final String TABLE_PHOTO_DETAIL = "photodetail";
    private static final String PHOTO_DETAIL_ID = "photoid";
    private static final String PHOTO_DETAIL_DATETAKEN = "datetaken";
    private static final String PHOTO_DETAIL_DATEUPLOADED = "dateuploaded";
    private static final String PHOTO_DETAIL_LOCATION = "location";
    private static final String PHOTO_DETAIL_CAMERA = "camera";
    private static final String PHOTO_DETAIL_FOCALLENGTH = "focallength";
    private static final String PHOTO_DETAIL_SHUTTERSPEED = "shutterspeed";
    private static final String PHOTO_DETAIL_APERTURE = "aperture";
    private static final String PHOTO_DETAIL_ISO = "iso";
    private static final String PHOTO_DETAIL_COPYRIGHT = "copyright";


    private static final String TABLE_PHOTO_COMMENTS = "photocomments";
    private static final String PHOTO_COMMENT_PHOTOID = "photoid";
    private static final String PHOTO_COMMENT_USERNAME = "username";
    private static final String PHOTO_COMMENT_DATE = "date";
    private static final String PHOTO_COMMENT_READERCOMMENT = "readercomment";

    private static final String TABLE_PHOTO_CATEGORY = "category";
    private static final String PHOTO_CATEGORY_NAME = "categoryname";

    private static final String TABLE_PHOTO_X_CATRGORY = "photoxcategory";
    private static final String PHOTO_X_CATEGORY_PHOTOID = "photoid";
    private static final String PHOTO_X_CATEGORY_CATEGORY = "category";


    public PosgreUtil() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }
        mConnection = getConnection();
        if (mConnection != null) {
            try {
                mStatement = mConnection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/exampledb", "dbuser", "dbuser");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close(){
        try {
            mStatement.close();
            mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //=================================================users=======================================
    public User getUser(String username) {
        StringBuilder sb = new StringBuilder();
        ResultSet resultSet = null;
        sb.append("select * from ")
                .append(TABLE_USERS)
                .append(" where " + USERS_USERNAME + " = '")
                .append(username)
                .append("';");
        try {
            resultSet = mStatement.executeQuery(sb.toString());
            if (resultSet.next()) {
                User user = new User();
                user.Username = resultSet.getString(USERS_USERNAME);
                user.Password = resultSet.getString(USERS_PASSWORD);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void putUser(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert into ")
                .append(TABLE_USERS)
                .append(" values ('")
                .append(user.Username)
                .append("','")
                .append(user.Password)
                .append("');");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("update " + TABLE_USERS + " set " + USERS_PASSWORD + " = '"
                + user.Password + "' where " + USERS_USERNAME + " = '" + user.Username + "';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        deletePhotoCommentsOfUser(username);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from " + TABLE_USERS + " where " + USERS_USERNAME + " = '"
                + username + "';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //============================================photo details=======================================
    public PhotoDetails getPhotoDetail(String photoId) {
        StringBuilder sb = new StringBuilder();
        ResultSet resultSet = null;
        sb.append("select * from ")
                .append(TABLE_PHOTO_DETAIL)
                .append(" where " + PHOTO_DETAIL_ID + " = '")
                .append(photoId)
                .append("';");
        try {
            resultSet = mStatement.executeQuery(sb.toString());
            if (resultSet.next()) {
                PhotoDetails photoDetails = new PhotoDetails();
                photoDetails.PhotoId = resultSet.getString(PHOTO_DETAIL_ID);
                photoDetails.DateTaken = resultSet.getString(PHOTO_DETAIL_DATETAKEN);
                photoDetails.DateUploaded = resultSet.getString(PHOTO_DETAIL_DATEUPLOADED);
                photoDetails.Location = resultSet.getString(PHOTO_DETAIL_LOCATION);
                photoDetails.Camera = resultSet.getString(PHOTO_DETAIL_CAMERA);
                photoDetails.FocalLength = resultSet.getString(PHOTO_DETAIL_FOCALLENGTH);
                photoDetails.ShutterSpeed = resultSet.getString(PHOTO_DETAIL_SHUTTERSPEED);
                photoDetails.Aperture = resultSet.getString(PHOTO_DETAIL_APERTURE);
                photoDetails.ISO = resultSet.getString(PHOTO_DETAIL_ISO);
                photoDetails.Copyright = resultSet.getString(PHOTO_DETAIL_COPYRIGHT);
                return photoDetails;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void putPhotoDetail(PhotoDetails photoDetails) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert into ")
                .append(TABLE_PHOTO_DETAIL + " (" + PHOTO_DETAIL_ID + "," +
                        PHOTO_DETAIL_DATETAKEN + "," + PHOTO_DETAIL_DATEUPLOADED + "," + PHOTO_DETAIL_LOCATION + "," + PHOTO_DETAIL_CAMERA + "," + PHOTO_DETAIL_FOCALLENGTH + "," + PHOTO_DETAIL_SHUTTERSPEED + "," + PHOTO_DETAIL_APERTURE + "," + PHOTO_DETAIL_ISO + "," + PHOTO_DETAIL_COPYRIGHT + ")")
                .append(" values ('").append(photoDetails.PhotoId).append("','").append(photoDetails.DateTaken).append("','").append(photoDetails.DateUploaded).append("','").append(photoDetails.Location).append("','").append(photoDetails.Camera).append("','").append(photoDetails.FocalLength).append("','").append(photoDetails.ShutterSpeed).append("','").append(photoDetails.Aperture).append("','").append(photoDetails.ISO).append("','").append(photoDetails.Copyright)
                .append("');");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePhotoDetail(PhotoDetails photoDetails) {
        StringBuilder sb = new StringBuilder();
        deletePhotoDetail(photoDetails.PhotoId);
        putPhotoDetail(photoDetails);
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePhotoDetail(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from " + TABLE_PHOTO_DETAIL + " where " + PHOTO_DETAIL_ID + " = '"
                + id + "';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //=========================================photo comments======================================
    public PhotoComments[] getPhotoComments(String photoId) {

        StringBuilder sb = new StringBuilder();
        ResultSet resultSet = null;
        sb.append("select * from ")
                .append(TABLE_PHOTO_COMMENTS)
                .append(" where " + PHOTO_DETAIL_ID + " = '")
                .append(photoId)
                .append("';");
        try {
            resultSet = mStatement.executeQuery(sb.toString());
            if (resultSet.next()) {
                ArrayList<PhotoComments> list = new ArrayList<>();
                do {
                    PhotoComments photoComments = new PhotoComments();
                    photoComments.userName = resultSet.getString(PHOTO_COMMENT_USERNAME);
                    photoComments.ReaderComment = resultSet.getString(PHOTO_COMMENT_READERCOMMENT);
                    photoComments.Date = resultSet.getString(PHOTO_COMMENT_DATE);
                    photoComments.photoId = photoId;
                    list.add(photoComments);
                } while (resultSet.next());
                return (PhotoComments[]) list.toArray();
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void putPhotoComments(PhotoComments photoComments) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert into ")
                .append(TABLE_PHOTO_COMMENTS + " (" + PHOTO_COMMENT_PHOTOID + "," +
                        PHOTO_COMMENT_DATE + "," + PHOTO_COMMENT_READERCOMMENT + "," + PHOTO_COMMENT_USERNAME + ")")
                .append(" values ('")
                .append(photoComments.photoId + "','" + photoComments.Date + "','" + photoComments.ReaderComment + "','" + photoComments.userName)
                .append("');");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePhotoComments(PhotoComments photoComments) {
        deletePhoto(photoComments.photoId);
        putPhotoComments(photoComments);
    }

    public void deletePhotoCommentsOfPhoto(String photoId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from " + TABLE_PHOTO_COMMENTS + " where " + PHOTO_ID + " = '").append(photoId).append("';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePhotoCommentsOfUser(String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from " + TABLE_PHOTO_COMMENTS + " where " + PHOTO_COMMENT_USERNAME + " = '").append(userName).append("';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //======================================== category =========================================
    public Category getCategory(String catrgoryName) {
        StringBuilder sb = new StringBuilder();
        ResultSet resultSet = null;
        sb.append("select * from ")
                .append(TABLE_PHOTO_X_CATRGORY)
                .append(" where " + PHOTO_X_CATEGORY_CATEGORY + " = '")
                .append(catrgoryName)
                .append("';");
        try {
            resultSet = mStatement.executeQuery(sb.toString());
            if (resultSet.next()) {
                Category category = new Category();
                category.CategoryName = catrgoryName;
                ArrayList<String> list = new ArrayList<>();
                do {
                    list.add(resultSet.getString(PHOTO_X_CATEGORY_PHOTOID));
                } while (resultSet.next());
                category.PhotoIds = (String[]) list.toArray();
                return category;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void putCategory(Category category) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert into ")
                .append(TABLE_PHOTO_CATEGORY + " (" + PHOTO_CATEGORY_NAME + ")")
                .append(" values ('")
                .append(category.CategoryName)
                .append("');");
        try {
            mStatement.execute(sb.toString());
            for (int i = 0; i < category.PhotoIds.length; i++) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append("insert into ")
                        .append(TABLE_PHOTO_X_CATRGORY + " (" + PHOTO_CATEGORY_NAME + "," + PHOTO_X_CATEGORY_PHOTOID + ")")
                        .append(" values ('")
                        .append(category.CategoryName + "','" + category.PhotoIds[i])
                        .append("');");
                mStatement.execute(sb1.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(Category category) {

    }

    public void deleteCategory(String categoryName) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from " + TABLE_PHOTO_X_CATRGORY + " where " + PHOTO_X_CATEGORY_CATEGORY + " = '").append(categoryName).append("';");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("delete from " + TABLE_PHOTO_CATEGORY + " where " + PHOTO_CATEGORY_NAME + " = '").append(categoryName).append("';");
            mStatement.execute(sb.toString());
            mStatement.execute(sb1.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //==========================================photo ===============================================
    public void putPhoto(Photo photo) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert into ")
                .append(TABLE_PHOTO + " (" + PHOTO_ID + "," +
                        PHOTO_TITLE + "," + PHOTO_AUTHOR + "," + PHOTO_CONTENT + "," + PHOTO_TAGS + ")")
                .append(" values ('")
                .append(photo.Id + "','" + photo.Title + "','" + photo.Author + "','" + photo.Essay + "','" + loadTags(photo.Tags))
                .append("');");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        putPhotoDetail(photo.photoDetails);
        for (int i = 0; i < photo.photoComments.length; i++) {
            putPhotoComments(photo.photoComments[i]);
        }
    }

    public Photo getPhoto(String id) {
        StringBuilder sb = new StringBuilder();
        ResultSet resultSet = null;
        sb.append("select * from ")
                .append(TABLE_PHOTO)
                .append(" where " + PHOTO_ID + " = '")
                .append(id)
                .append("';");
        try {
            resultSet = mStatement.executeQuery(sb.toString());
            if (resultSet.next()) {
                Photo photo = new Photo();
                photo.Title = resultSet.getString(PHOTO_TITLE);
                photo.Author = resultSet.getString(PHOTO_AUTHOR);
                photo.Essay = resultSet.getString(PHOTO_CONTENT);
                photo.Id = id;
                photo.Tags = parseTags(resultSet.getString(PHOTO_TAGS));
                photo.photoComments = getPhotoComments(id);
                photo.photoDetails = getPhotoDetail(id);
                return photo;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePhoto(Photo photo) {
        deletePhoto(photo.Id);
        putPhoto(photo);
    }

    public void deletePhoto(String photoId) {
        deletePhotoCommentsOfPhoto(photoId);
        deletePhotoDetail(photoId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from " + TABLE_PHOTO + " where " + PHOTO_ID + " = '").append(photoId).append("';");
        try {
            mStatement.execute(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<String> parseTags(String s) {
        String[] tags = s.split("|");
        ArrayList<String> taglist = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            taglist.add(tags[i]);
        }
        return taglist;
    }

    private String loadTags(List<String> tags) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i));
            sb.append("|");
        }
        return sb.toString();
    }

}
