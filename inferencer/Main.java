package inferencer;

import java.awt.*;

/**
 * inferencer.Main program, instantiates model, view and
 * controller in a new Runnable instance
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class Main {
    public static void main(String[] args) {

        // Lambda expression to substitute new Runnable()
        EventQueue.invokeLater(() -> {
            // Model-View-Controller pattern
            View view = new View();
            Model model = new Model();
            Controller controller = new Controller(model, view);
        });
    }
}
