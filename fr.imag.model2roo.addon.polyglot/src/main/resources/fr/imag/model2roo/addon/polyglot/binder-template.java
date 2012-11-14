__INIT_IMPORTS__
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import javax.servlet.ServletException;
__END_IMPORTS__

__INIT_BINDER__

    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
 
        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
__END_BINDER__