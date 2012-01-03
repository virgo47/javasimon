package org.javasimon.console.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javasimon.console.Getter;
import org.javasimon.console.ValueFormatter;

/**
 * JavaScript Object
 * @author gquintana
 */
public class ObjectJS extends AnyJS {
    private List<Attribute> attributes;
    private Map<String,Attribute> attributesByName;
    public ObjectJS() {
        this.attributes=new ArrayList<Attribute>();
        this.attributesByName=new HashMap<String, Attribute>();
    }

    public void setAttribute(String name, AnyJS value) {
        assert name!=null;
        assert value!=null;
        Attribute attribute=attributesByName.get(name);
        if (attribute==null) {
            attribute=new Attribute(name,value);
            attributes.add(attribute);
            attributesByName.put(name, attribute);
        } else {
            attribute.value=value;
        }
    }
    public AnyJS getAttribute(String name) {
        assert name!=null;
        Attribute attribute=attributesByName.get(name);
        return attribute==null?null:attribute.value;
    }
    @Override
    public void write(Writer writer) throws IOException {
        writer.write("{");
        boolean first=true;
        for(Attribute attribute:attributes) {
            if (first) {
                first=false;
            } else {
                writer.write(",");
            }
            writeString(writer,attribute.name);
            writer.write(":");
            attribute.value.write(writer);
        }
        writer.write("}");
    }
    public static ObjectJS create(Object o, ValueFormatter valueFormatter) {
        if (o==null) {
            return null;
        } else {
            ObjectJS objectJS=new ObjectJS();
            for(Getter getter:Getter.getGetters(o.getClass())) {
                String propertyName=getter.getName();
                Class propertyType = getter.getType();
                Object propertyValue = getter.get(o);
                AnyJS propertyJS = null;
                if (propertyType.equals(Integer.class)||propertyType.equals(Integer.TYPE)) {
                    propertyJS=SimpleJS.createNumber((Integer) propertyValue, valueFormatter);
                } else if (propertyType.equals(Long.class)||propertyType.equals(Long.TYPE)) {
                    propertyJS=SimpleJS.createNumber((Long) propertyValue, valueFormatter);
                } else if (propertyType.equals(String.class)) {
                    propertyJS=SimpleJS.createString((String) propertyValue, valueFormatter);
                } else if (Enum.class.isAssignableFrom(propertyType)) {
                    propertyJS=SimpleJS.createEnum((Enum) propertyValue, valueFormatter);
                } 
                if (propertyJS!=null) {
                    objectJS.setAttribute(propertyName,propertyJS);
                }
            }
            return objectJS;
        }
    }
    private static class Attribute {
        private final String name;
        private AnyJS value;
        public Attribute(String name, AnyJS value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Attribute other = (Attribute) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return this.name==null?0:this.name.hashCode();
        }
        
    }
    
}
