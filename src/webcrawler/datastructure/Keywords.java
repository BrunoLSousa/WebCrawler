/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bruno
 */
public class Keywords {
    
    private String type;
    private List<String> kwd;
    
    public Keywords(){
        this.kwd = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getKeywords() {
        return kwd;
    }

    public void setKeywords(List<String> keywords) {
        this.kwd = keywords;
    }
    
}
