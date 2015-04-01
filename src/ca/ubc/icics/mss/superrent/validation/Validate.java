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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author shilpa
 */
public class Validate {
    
    /**
     * Validates if a text field is empty or not
     * @param tf Text field that has to be checked
     * @return true or false
     */
    public static boolean isEmptyTextField (TextField tf) {
        boolean valid = false;
        if (tf.getText() != null && tf.getText().trim().length() > 0) {
            valid = true;
        }
        return valid;
    }
    
    /**
     * Validates if a text field is empty or not but also adds css to the field
     * and displays an error message in the label provided
     * @param tf text field that has to be checked
     * @param error the error label that will show the error message
     * @return true or false
     */
    public static boolean isEmptyTextField (TextField tf, Label error) {
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
     * Validates if a date picker is empty or not
     * @param dateField Date picker that has to be checked
     * @return true or false
     */
    public static boolean isEmptyDateField (DatePicker dateField) {
        boolean valid = false;
        if (dateField.getValue() != null) {
            valid = true;
        }
        return valid;
    }
    
    /**
     * Validates if a date picker is empty or not
     * @param dateField Date picker that has to be checked
     * @param error the error label that will show the error message
     * @return true or false
     */
    public static boolean isEmptyDateField(DatePicker dateField, Label error) {
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
     * Validates if a choice box is empty or not
     * @param choiceBox that has to be checked
     * @return true or false
     */
    public static boolean isEmptyChoiceBox (ChoiceBox choiceBox) {
        boolean valid = false;
        if (choiceBox.getValue() != null) {
            valid = true;
        }
        return valid;
    }
    
    /**
     * Validates if a choice box is empty or not
     * @param choiceBox  that has to be checked
     * @param error the error label that will show the error message
     * @return true or false
     */
    public static boolean isEmptyChoiceBox(ChoiceBox choiceBox, Label error) {
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
    
    /**
     * Validates if a phone number in a text field is a valid Canadian phone 
     * number
     * @param phone Text field that holds the phone number to be checked
     * @return true or false
     */
    public static boolean isValidPhoneNumber (TextField phone) {
        boolean valid = false;
        // Check if the phone number is valid.
        if (phone.getText() != null && 
                phone.getText().trim().length() > 0 && 
                phone.getText().matches("^[0-9]{3}\\s[0-9]{3}\\s[0-9]{4}$")) {
            valid = true;
        }
        return valid;
    }
    
    /**
     * Validates if a phone number in a text field is a valid Canadian phone 
     * number
     * @param phone Text field that holds the phone number to be checked
     * @param error the error label that will show the error message
     * @return true or false
     */
    public static boolean isValidPhoneNumber(TextField phone, Label error) {
        boolean valid = false;
        ObservableList<String> styleClass = phone.getStyleClass();
        
        // Check if the phone number is valid.
        if (phone.getText() != null && 
                phone.getText().trim().length() > 0 && 
                phone.getText().matches("^[0-9]{3}\\s[0-9]{3}\\s[0-9]{4}$")) {
            valid = true;
        }
        
        if (phone.getText() != null && phone.getText().trim().length() > 0) {
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
