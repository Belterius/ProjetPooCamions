/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import metier.CoordinateCSV;
import metier.SwapActionsCSV;

/**
 *
 * @author belterius
 */
public class SwapActionParser {
    private SwapActionsCSV mySwapActions;
    private String currentLine;
    private String separator = ";";

    public SwapActionsCSV getMySwapActions() {
        return mySwapActions;
    }
    
        public SwapActionParser(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            mySwapActions = new SwapActionsCSV();
            currentLine = br.readLine(); //Remove the line with column name information
            while ((currentLine = br.readLine()) != null) {
                String[] swapAction = currentLine.split(separator);
                mySwapActions.addAction(swapAction[0], Integer.valueOf(swapAction[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
