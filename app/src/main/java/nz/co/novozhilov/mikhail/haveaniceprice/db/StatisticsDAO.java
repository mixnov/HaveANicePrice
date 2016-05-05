package nz.co.novozhilov.mikhail.haveaniceprice.db;


/**
 * Created by Mikhail on 04.05.2016.
 * Data access methods for Statistics entities
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public class StatisticsDAO {

    /**
     * Generate sql query string
     *
     * @param productFilter - additional join and/or where clause
     * @return string of query
     */
    static String getStatisticsQuery(String productFilter) {
        //SELECT tsh.title, tp.title, ts.date, ts.special, ts.price, ts.std_price, ts.disc_price, ts.old_price, ts.save_price  FROM stat ts JOIN products tp ON ts.product_id = tp._id JOIN shops tsh ON tp.shop_id = tsh._id
        return "SELECT tst." + DBHelper.COLUMN_ID +         //0
                ", tsh." + DBHelper.COLUMN_SH_TITLE +       //1
                ", tpr." + DBHelper.COLUMN_P_TITLE +        //2
                ", tst." + DBHelper.COLUMN_ST_DATE +        //3
                ", tst." + DBHelper.COLUMN_ST_SPECIAL +     //4
                ", tst." + DBHelper.COLUMN_ST_PRICE +       //5
                ", tst." + DBHelper.COLUMN_ST_STD_PRICE +   //6
                ", tst." + DBHelper.COLUMN_ST_DISC_PRICE +  //7
                ", tst." + DBHelper.COLUMN_ST_OLD_PRICE +   //8
                ", tst." + DBHelper.COLUMN_ST_SAVE_PRICE +  //9
                " FROM " + DBHelper.TABLE_STATISTICS + " tst " +
                " JOIN " + DBHelper.TABLE_PRODUCTS +
                " tpr ON tst." + DBHelper.COLUMN_ST_PRODUCT_ID +
                " = tpr." + DBHelper.COLUMN_ID +
                " JOIN " + DBHelper.TABLE_SHOPS +
                " tsh ON tpr." + DBHelper.COLUMN_P_SHOP_ID +
                " = tsh." + DBHelper.COLUMN_ID +
                productFilter +
                " ORDER BY tsh." + DBHelper.COLUMN_ID + ", tpr." + DBHelper.COLUMN_ID;
    }

//    static ArrayList<Statistics> getQuestions(Context context, String whereClause) {
//        // open connection to the database
//        DBHelper dbHandler = new DBHelper(context);
//        SQLiteDatabase db = dbHandler.getWritableDatabase();
//        // create empty array list for questions
//        ArrayList<Statistics> questions = new ArrayList<>();
//        // create custom query to get data from questions and answers
//        String selectQuestions = getStatisticsQuery(whereClause);
//        // execute query
//        Cursor cursor = db.rawQuery(selectQuestions, null);
//
//        int current_id = -1;
//        Statistics statistics = null;
//
//        // loop through the results
//        if (cursor.moveToFirst()) {
//            do {
//                int qId = cursor.getInt(0);
//                if (qId != current_id) {
//                    String qText = cursor.getString(1);
//                    int qTestType = cursor.getInt(2);
//                    int qCategory = cursor.getInt(3);
//                    boolean qMultiple = (cursor.getInt(4) == 1);
//                    String qExplain = cursor.getString(5);
//
//                    // create question
//                    statistics = new Statistics(qId, qText, qCategory, new ArrayList<Answer>(),
//                            qMultiple, qTestType, qExplain);
//
//                    // add question to the list of questions
//                    questions.add(question);
//                    current_id = qId;
//                }
//                int aId = cursor.getInt(6);
//                String aText = cursor.getString(7);
//                boolean aCorrect = (cursor.getInt(8) == 1);
//
//                // create answer
//                Answer answer = new Answer(aId, aText, aCorrect);
//                // add answer to the question's array list
//                assert question != null;
//                question.addAnswer(answer);
//            } while (cursor.moveToNext());
//        }
//        //close connection
//        cursor.close();
//        db.close();
//        dbHandler.close();
//
//        return questions;
//    }
//
//    /**
//     * get all questions for test type
//     *
//     * @param context  - app context
//     * @param testType - java test, C test, etc.
//     * @return list of questions
//     */
//    static ArrayList<Question> getAllQuestions(Context context, int testType) {
//        String whereClause = " WHERE tq." + DBHelper.COLUMN_TEST + " = " + testType;
//        return getQuestions(context, whereClause, "");
//    }
//
//    /**
//     * get questions by category
//     *
//     * @param context  - app context
//     * @param testType - java test, C test, etc.
//     * @return list of questions
//     */
//    static ArrayList<Question> getQuestionsByCategory(Context context, int testType, int categoryId) {
//        String whereClause = " WHERE tq." + DBHelper.COLUMN_TEST + " = " + testType +
//                " AND tq." + DBHelper.COLUMN_CATEGORY_FK + " = " + categoryId;
//        return getQuestions(context, whereClause, "");
//    }
//
//    /**
//     * get all questions with wrong answer
//     *
//     * @param context  - app context
//     * @param testType - java test, C test, etc.
//     * @return list of questions
//     */
//    static ArrayList<Question> getFailedQuestions(Context context, int testType) {
//        String whereClause = " INNER JOIN " + DBHelper.TABLE_MISTAKES + " te ON te." +
//                DBHelper.COLUMN_ID + " = tq." + DBHelper.COLUMN_ID +
//                " WHERE tq." + DBHelper.COLUMN_TEST + " = " + testType;
//        return getQuestions(context, whereClause, "");
//    }
//
//
//    /**
//     * Add question to mistake table
//     *
//     * @param questionID - question's id in the database
//     */
//    static void addMistake(Context context, int questionID) {
//        DBHelper dbHandler = new DBHelper(context);
//        SQLiteDatabase db = dbHandler.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(DBHelper.COLUMN_ID, questionID);
//
//        //insert into db (if id is already there - ignore)
//        db.insertWithOnConflict(DBHelper.TABLE_MISTAKES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//
//        //close connection
//        db.close();
//        dbHandler.close();
//    }
//
//
//    /**
//     * Remove question from mistake table
//     *
//     * @param questionID - question's id in the database
//     */
//    static void removeMistake(Context context, int questionID) {
//        DBHelper dbHandler = new DBHelper(context);
//        SQLiteDatabase db = dbHandler.getWritableDatabase();
//
//        String whereClause = DBHelper.COLUMN_ID + "=?";
//        String[] whereArgs = new String[]{String.valueOf(questionID)};
//
//        //insert into db (if id is already there - ignore)
//        db.delete(DBHelper.TABLE_MISTAKES, whereClause, whereArgs);
//
//        //close connection
//        db.close();
//        dbHandler.close();
//    }
//
//    /**
//     * Remove all questions from mistake table
//     */
//    static void removeAllMistakes(Context context) {
//        DBHelper dbHandler = new DBHelper(context);
//        SQLiteDatabase db = dbHandler.getWritableDatabase();
//        // delete all
//        db.execSQL("delete from " + DBHelper.TABLE_MISTAKES);
//        // free allocated space
//        db.execSQL("vacuum");
//        //close connection
//        db.close();
//        dbHandler.close();
//    }
//
//
//    /**
//     * Get exact quantity of random questions
//     *
//     * @param context       - app context
//     * @param testType      - java test, C test, etc.
//     * @param questionCount - number of questions to return
//     * @return list of questions
//     */
//    static ArrayList<Question> getShuffledQuestions(Context context, int testType, int questionCount) {
//        ArrayList<Question> questions = getAllQuestions(context, testType);
//        //shuffle result
//        Collections.shuffle(questions);
//        for (int i = questionCount; i < questions.size(); i++) {
//            questions.remove(i);
//        }
//        return questions;
//    }
}

