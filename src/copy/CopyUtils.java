package copy;

public class CopyUtils {
    public static <T> T deepCopy(T obj)  {
        CopyUtilsInner copier = new CopyUtilsInner();
        return copier.deepCopy(obj);
    }
}
