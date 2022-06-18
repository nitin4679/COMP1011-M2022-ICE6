package ca.georgiancollege.comp1011m2022ice6;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.ArrayList;

// singleton
public class DBManager
{    /********************** SINGLETON SECTION **************************/
    //step 1- private static instance member variable
    private static DBManager m_instance = null;
    //step2 - make the default constructor private
    private DBManager(){}
    //step3 - create a public static entry point / instance method
    public static DBManager Instance()
    {
        //step4 - check if the private instance member variable is null
        if(m_instance == null)
        {
            //step5 - Instantiate a new DBManager instance
            m_instance = new DBManager();
        }
        return m_instance;
    }
    /********************************************************************* */



    // private instance member variables
    private String m_user = "student";
    private String m_password = "123456";
    private String m_connectURL = "jdbc:mysql://localhost:3306/comp1011m2022";

    /**
     * This method inserts a Vector2D object to the Database
     * @param vector2D
     * @return
     * @throws SQLException
     */
    public int insertVector2D(Vector2D vector2D) throws SQLException {
        int vectorID = -1; // if this method returns -1 - it means that something went wrong
        //initializing the result set object
        ResultSet resultSet = null;

        //create  a query string
        String sql = "Insert into vectors(X,Y, Magnitude) values(?,?,?);";

        try
        ( /*head of the try / catch block */
            Connection connection = DriverManager.getConnection(m_connectURL, m_user, m_password);
            PreparedStatement statement = connection.prepareStatement(sql, new String[] {"vectorID"});

        )
        {

            // configure prepared  statement
            statement.setFloat(1,vector2D.getX());
            statement.setFloat(2, vector2D.getY());
            statement.setFloat(3, vector2D.getMagnitude());

            //run the command on the Database

            statement.executeUpdate();

            //get the vectorID
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next())
            {
                //get the VectorID from the database
                vectorID = resultSet.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (resultSet != null)
            {
                resultSet.close();
            }
        }

        return vectorID;
    }

    /**
     * This method reads the vectors table from the MySQL database and returns an
     * ArrayList of Vector2D type.
     * @return
     */
    public ArrayList<Vector2D> readVectorTable()
    {
        // Instantiates an ArrayList collection of type Vector2D  (empty container)
        ArrayList<Vector2D> vectors = new ArrayList<Vector2D>();

        String sql = "select vectors.vectorID, X, Y from vectors group by vectors.vectorID";

        try (Connection connection = DriverManager.getConnection(m_connectURL, m_user, m_password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);)
        {
            // while there is another record ..... loop
            while (resultSet.next())
            {
                //decode data from the database table
                int vectorID = resultSet.getInt("vectorID");
                float X = resultSet.getFloat("X");
                float Y = resultSet.getFloat("Y");

                // create an anonymous Vector2D object with the data from the database
                // then add the new Vector2D object to the vectors ArrayList
                vectors.add(new Vector2D(vectorID, X, Y));

            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }

        return vectors;
    }

    /**
     * This method returns the Bar chart data from the database
     * @return
     */
    public XYChart.Series<String, Float> getMagnitude()
    {  // step 1 - create a Series
        XYChart.Series<String, Float> magnitudes = new XYChart.Series<>();
        magnitudes.setName("2022");
        // Step 2 - get the data from the Database
        ArrayList<Vector2D> vectors = readVectorTable();
        // step 3 - for each vector in the data .... loop and add it to the Series

        // mock data example
        /*
        magnitudes.getData().add(new XYChart.Data<>("0,0", 0.0f));
        magnitudes.getData().add(new XYChart.Data<>("10,20", 10.0f));
        magnitudes.getData().add(new XYChart.Data<>("30,40", 20.0f));
        magnitudes.getData().add(new XYChart.Data<>("50,80", 30.0f));
        */

        for (var vector : vectors)
        {
            var chartData = new XYChart.Data<>(vector.toOneDecimalString(), vector.getMagnitude());
            magnitudes.getData().add(chartData);
        }
        return magnitudes;
    }
}
