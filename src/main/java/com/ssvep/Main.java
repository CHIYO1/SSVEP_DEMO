/**
 * 这个文件是主应用程序入口.
 * 
 * @author 石振山
 * @version 1.0.0
 */
package com.ssvep;

import com.ssvep.config.DatabaseConnection;

public class Main {
    public static void main(String[] args){
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    }
}
