/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.validation;

import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *
 * @author shilpa
 */
public class Validate {
    public static boolean isEmptyTextField (TextField tf) {
        boolean valid = false;
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText() == null || tf.getText().trim().length()==0) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = true;
        }
        return valid;
    }
    
    /**
     * 
     * @param dateField
     * @return 
     */
    public static boolean isEmptyDateField(DatePicker dateField) {
        boolean valid = false;
        ObservableList<String> styleClass = dateField.getStyleClass();
        if (dateField.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = true;                   
        }
        
        return valid;
    }
    
    /**
     * 
     * @param choiceBox
     * @return 
     */
    public static boolean isEmptyChoiceBox(ChoiceBox choiceBox) {
        boolean valid = false;
        ObservableList<String> styleClass = choiceBox.getStyleClass();
        if (choiceBox.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = true;
        }
        return valid;
    }
    
}
