import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class forEachMethod {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(4, 5, 6, 72, 21, 5);
        List<String> names = List.of("Yashu", "Parul", "Kiran", "Navin");

        // Consumer<Integer> con = new Consumer<Integer>() {

        // public void accept(Integer n) {
        // System.out.println(n);
        // }
        // };

        // Consumer<Integer> con= n -> System.out.println(n);

        // nums.forEach(con);

        nums.forEach(n -> System.out.println(n));
        names.forEach(name -> System.out.println(name));

        // | Feature | `Arrays.asList()` | `List.of()` |
        // | --------------- | ----------------- | -------------------------- |
        // | Size | Fixed | Fixed |
        // | Add/Remove | ❌ Not allowed | ❌ Not allowed |
        // | Modify (set) | ✅ Allowed | ❌ Not allowed |
        // | Mutability | Partially mutable | Fully immutable |
        // | Backed by array | ✅ Yes | ❌ No |
        // | Null elements | ✅ Allowed | ❌ Not allowed (throws NPE) |
        // | Introduced in | Java 1.2 | Java 9 |

    }
}
