/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logging;

import java.util.ArrayList;
import java.util.List;

@LoggingInterceptor
public class ShoppingCart {
    private List<String> items;
    
    public ShoppingCart() {
        items = new ArrayList<String>();
    }
    
    public void addItem(String item) {
        items.add(item);
    }
    
    public void deleteItem(String item) {
        items.remove(item);
    }
    
    public void checkout() {
        System.out.println(items + " checking out");
    }
}
