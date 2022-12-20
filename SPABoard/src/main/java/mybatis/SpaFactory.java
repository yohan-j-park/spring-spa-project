package mybatis;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
public class SpaFactory {
    private static SqlSessionFactory factory;
    
    static {
        try {
            Reader r = Resources.getResourceAsReader("mybatis/config.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
        }catch(Exception ex ) {
            ex.printStackTrace();
        }
    }
    public static SqlSessionFactory getFactory() {
        return factory;
    }
    
}