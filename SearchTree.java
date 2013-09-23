import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: David
 * Date: 2013-09-23
 * Time: 10:36
 */
public class SearchTree extends PriorityQueue<State> {

    public SearchTree() {
        super(25, new Comparator<State>() {

            @Override
            public int compare(State s1, State s2) {
                if (someName(s1) < someName(s2)) return -1;
                else if (someName(s1) > someName(s2)) return 1;
                return 0;
            }

            // A method that should return some scalar
            public double someName(State state) {
                return 0.1;
            }

        });
    }

    public void filterAdd(State state) {
        //if (someStatement) this.add(state);
    }
}
