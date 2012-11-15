__INIT_IMPORTS__
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
__END_IMPORTS__


__INIT_METHOD__
    @RequestMapping(value = "/show__PROPERTY__/{id}", produces = "text/html")
    public String get__PROPERTY__Content(@PathVariable("id") BigInteger id, HttpServletResponse response, Model uiModel) {
        __ENTITY__ __ENTITY_LOWER__;
        OutputStream outputStream;
        
        __ENTITY_LOWER__ = __ENTITY_LOWER__Repository.findOne(id);
        try {
            response.setHeader("Content-Disposition", "inline;");
            outputStream = response.getOutputStream();
            response.setContentType("__CONTENT_TYPE__");
            IOUtils.copy(new ByteArrayInputStream(__ENTITY_LOWER__.get__PROPERTY__()) , outputStream);
            outputStream.flush();
        } catch(Exception e) {
        }
        
        return null;
    }
__END_METHOD__