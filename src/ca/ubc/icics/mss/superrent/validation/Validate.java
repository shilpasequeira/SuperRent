/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.validation;

import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
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
        boolean valid = true;
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText() == null || tf.getText().trim().length() == 0) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;
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
        boolean valid = true;
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText() == null || tf.getText().trim().length() == 0) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            error.setVisible(true);
            error.setText("This field is required");
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;
            error.setVisible(false);
        }
        return valid;
    }
    
    /**
     * Validates if a date picker is empty or not
     * @param dateField Date picker that has to be checked
     * @return true or false
     */
    public static boolean isEmptyDateField (DatePicker dateField) {
        boolean valid = true;
        ObservableList<String> styleClass = dateField.getStyleClass();
        if (dateField.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;              
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
        boolean valid = true;
        ObservableList<String> styleClass = dateField.getStyleClass();
        if (dateField.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            error.setVisible(true);
            error.setText("This field is required");
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false; 
            error.setVisible(false);               
        }
        return valid;
    }
    
    /**
     * Validates if a choice box is empty or not
     * @param comboBox that has to be checked
     * @return true or false
     */
    public static boolean isEmptyComboBox (ComboBox comboBox) {
        boolean valid = true;
        ObservableList<String> styleClass = comboBox.getStyleClass();
        if (comboBox.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;
        }
        return valid;
    }
    
    /**
     * Validates if a choice box is empty or not
     * @param comboBox  that has to be checked
     * @param error the error label that will show the error message
     * @return true or false
     */
    public static boolean isEmptyComboBox(ComboBox comboBox, Label error) {
        boolean valid = true;
        ObservableList<String> styleClass = comboBox.getStyleClass();
        if (comboBox.getValue() == null) {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            error.setVisible(true);
            error.setText("This field is required");
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;
            error.setVisible(false);
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
        ObservableList<String> styleClass = phone.getStyleClass();
        
        // Check if the phone number is valid.
        if (phone.getText() != null && 
                phone.getText().trim().length() > 0 && 
                phone.getText().matches("^[0-9]{10}$")) {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = true;
        } else {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
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
                phone.getText().matches("^[0-9]{10}$")) {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            error.setVisible(false);
            valid = true;
        } else {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            error.setVisible(true);
            error.setText("Invalid phone number");
        }
        return valid;
    }
}
