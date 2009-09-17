/*
 * SimpleEntity.java
 *
 * @author Marina Vatkina
 */

package sample.ejb.embedded.persistence;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
    @NamedQuery(name = "SimpleEntity.findAll", query = "select e from SimpleEntity e")
})
public class SimpleEntity {
    
    @Id 
    private int id;
    private String name;

    public SimpleEntity(int id) {
        this.id = id;
        name = "Entity number " + id + " created at " + new Date();
    }
    
    public SimpleEntity() {
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
