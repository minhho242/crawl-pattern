package model;

import org.jsoup.nodes.Document;

/**
 * Description
 *
 * @author minhho242 on 3/23/15.
 */
public abstract class Item {

    private String _itemName;
    private String _value;
    
    public Item(String itemName) {
        _itemName = itemName;
        
    }
    
    public String getValue() {
        return _value;
        
    }
    
    public Item setValue(String value) {
        this._value = value;
        return this;
    }
    
    public String getName() {
        return _itemName;
        
    }
    
    public abstract String extractValue(Document document);
}
