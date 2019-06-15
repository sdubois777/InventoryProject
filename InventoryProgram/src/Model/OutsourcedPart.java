/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author hypas
 */
//Getter and setter for Outsourced Parts
public class OutsourcedPart extends Part {
    
    private final StringProperty companyName;
    
    public OutsourcedPart() {
        super();
        companyName = new SimpleStringProperty();
    }
    
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    
    public String getCompanyName() {
        return this.companyName.get();
    }
}
