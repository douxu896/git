package dou;

import java.io.ByteArrayInputStream;  
import java.io.IOException;  
  
import org.xml.sax.EntityResolver;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
/**
 * <p>
 * <code>IgnoreDTDEntityResolver</code> is ignore DTDEntity 
 * </p>
 * 
 * @author <a href="bjtu:15121684@bjtu.edu.cn">Emily </a>
 * @version $Revision: 2.0 $
 */
public class IgnoreDTDEntityResolver implements EntityResolver {  
  
 @Override  
 public InputSource resolveEntity(String publicId, String systemId)throws SAXException,IOException {  
        return new InputSource(  
             new ByteArrayInputStream(  
                   "<?xml version='1.0' encoding='UTF-8'?>".getBytes()  
    ));  
 }  
  
}  